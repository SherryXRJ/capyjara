package com.capy.capyjara.gateway.config.oauth2;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.R2dbcReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ReactiveRedis实现存储OAuth2已授权信息(access_token, refresh_token等)
 *
 * @see InMemoryReactiveOAuth2AuthorizedClientService
 * @see R2dbcReactiveOAuth2AuthorizedClientService
 * @see MultiDeviceServerOAuth2AuthorizationRequestResolver
 */
@Slf4j
public class ReactiveRedisOAuth2AuthorizedClientService implements ReactiveOAuth2AuthorizedClientService {

    public final static String redisReactiveOAuth2AuthorizedKey = "reactive:oauth2:authorized:";

    /**
     * redis中授权信息有效时间
     */
    @Setter
    private Duration oAuth2AuthorizedLiveDuration = Duration.ofSeconds(30);

    protected final ReactiveClientRegistrationRepository clientRegistrationRepository;

    protected final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Setter
    protected BiFunction<OAuth2AuthorizedClient, Authentication, Map<String, Object>> oAuth2AuthorizedClient2HashConverter = new DefaultOAuth2AuthorizedClient2HashConverter();

    @Setter
    protected Function<Map<Object, Object>, Mono<OAuth2AuthorizedClient>> hash2AuthorizedClientConverter = new DefaultHash2OAuth2AuthorizedClientConverter();

    public ReactiveRedisOAuth2AuthorizedClientService(ReactiveRedisTemplate<String, Object> reactiveRedisTemplate, ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.clientRegistrationRepository = reactiveClientRegistrationRepository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends OAuth2AuthorizedClient> Mono<T> loadAuthorizedClient(String clientRegistrationId, String principalName) {
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return (Mono<T>) Mono.deferContextual(context -> Mono.just(context.get(ServerWebExchange.class)))
                .map(exchange -> exchange.getRequest().getCookies().get("deviceId"))
                .filter(CollectionUtil::isNotEmpty)
                //  todo: 会话失效，重定向到登录页
                .switchIfEmpty(Mono.error(() -> new RuntimeException("deviceId cannot be empty")))
                .map(deviceId -> redisReactiveOAuth2AuthorizedKey + deviceId.get(0).getValue())
                .flatMap(identifier -> reactiveRedisTemplate.opsForHash().entries(identifier).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .flatMap(hash -> hash2AuthorizedClientConverter.apply(hash));
    }

    @Override
    public Mono<Void> saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        Assert.notNull(authorizedClient, "authorizedClient cannot be null");
        Assert.notNull(principal, "principal cannot be null");

        return Mono.deferContextual(context -> Mono.just(context.get(ServerWebExchange.class)))
                .map(exchange -> exchange.getRequest().getCookies().get("deviceId"))
                .filter(CollectionUtil::isNotEmpty)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("deviceId cannot be empty")))
                .map(deviceId -> redisReactiveOAuth2AuthorizedKey + deviceId.get(0).getValue())
                .flatMap(identifier ->
                        reactiveRedisTemplate.opsForHash().putAll(identifier, oAuth2AuthorizedClient2HashConverter.apply(authorizedClient, principal))
                                .flatMap(success -> reactiveRedisTemplate.expire(identifier, oAuth2AuthorizedLiveDuration))
                )
                .doOnSuccess(success -> log.debug("saveAuthorizedClient to redis:{}", success))
                .then(Mono.empty());
    }

    @Override
    public Mono<Void> removeAuthorizedClient(String clientRegistrationId, String principalName) {
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");

        return Mono.deferContextual(context -> Mono.just(context.get(ServerWebExchange.class)))
                .flatMap(ServerWebExchange::getSession)
                .map(session -> redisReactiveOAuth2AuthorizedKey + principalName)
                .flatMap(reactiveRedisTemplate::delete)
                .then(Mono.empty());
    }

    private static final class DefaultOAuth2AuthorizedClient2HashConverter implements BiFunction<OAuth2AuthorizedClient, Authentication, Map<String, Object>> {

        @Override
        public Map<String, Object> apply(OAuth2AuthorizedClient oAuth2AuthorizedClient, Authentication authentication) {
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("clientRegistrationId", oAuth2AuthorizedClient.getClientRegistration().getRegistrationId());
            map.put("principalName", authentication.getName());

            OAuth2AccessToken oAuth2AccessToken = oAuth2AuthorizedClient.getAccessToken();
            if (Objects.nonNull(oAuth2AccessToken)) {
                map.put("accessTokenType", oAuth2AccessToken.getTokenType().getValue());
                map.put("accessTokenValue", oAuth2AccessToken.getTokenValue());
                map.put("accessTokenIssuedAt", oAuth2AccessToken.getIssuedAt().toString());
                map.put("accessTokenExpiresAt", oAuth2AccessToken.getExpiresAt().toString());
                map.put("accessTokenScopes", oAuth2AccessToken.getScopes());
            }

            OAuth2RefreshToken oAuth2RefreshToken = oAuth2AuthorizedClient.getRefreshToken();
            if (Objects.nonNull(oAuth2RefreshToken)) {
                map.put("refreshTokenValue", oAuth2RefreshToken.getTokenValue());
                map.put("refreshTokenIssuedAt", oAuth2RefreshToken.getIssuedAt().toString());
            }

            return map;
        }
    }

    private final class DefaultHash2OAuth2AuthorizedClientConverter implements Function<Map<Object, Object>, Mono<OAuth2AuthorizedClient>> {

        @Override
        @SuppressWarnings("unchecked")
        public Mono<OAuth2AuthorizedClient> apply(Map<Object, Object> map) {
            if (CollectionUtils.isEmpty(map)) {
                return Mono.empty();
            }

            String clientRegistrationId = (String) map.get("clientRegistrationId");

            String principalName = (String) map.get("principalName");

            String accessTokenType = (String) map.get("accessTokenType");
            String accessTokenValue = (String) map.get("accessTokenValue");
            String accessTokenIssuedAt = (String) map.get("accessTokenIssuedAt");
            String accessTokenExpiresAt = (String) map.get("accessTokenExpiresAt");
            Set<String> accessTokenScopes = (Set<String>) map.getOrDefault("accessTokenScopes", Collections.emptySet());

            OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessTokenValue,
                    Objects.nonNull(accessTokenIssuedAt) ? Instant.parse(accessTokenIssuedAt) : null,
                    Objects.nonNull(accessTokenExpiresAt) ? Instant.parse(accessTokenExpiresAt) : null,
                    accessTokenScopes);

            String refreshTokenValue = (String) map.get("refreshTokenValue");
            String refreshTokenIssuedAt = (String) map.get("refreshTokenIssuedAt");

            OAuth2RefreshToken oAuth2RefreshToken = new OAuth2RefreshToken(refreshTokenValue, Instant.parse(refreshTokenIssuedAt));

            return clientRegistrationRepository.findByRegistrationId(clientRegistrationId)
                    .switchIfEmpty(Mono.error(() -> new RuntimeException("clientRegistration is not exist")))
                    .map(clientRegistration -> new OAuth2AuthorizedClient(clientRegistration, principalName, oAuth2AccessToken, oAuth2RefreshToken));

        }
    }



}
