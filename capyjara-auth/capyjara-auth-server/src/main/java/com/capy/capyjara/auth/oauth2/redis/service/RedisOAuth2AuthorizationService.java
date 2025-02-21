package com.capy.capyjara.auth.oauth2.redis.service;

import com.capy.capyjara.auth.oauth2.redis.entity.OAuth2AuthorizationGrantAuthorization;
import com.capy.capyjara.auth.oauth2.redis.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    public final static String REDIS_AUTHORIZATION_KEY = "oauth2_authorization";

    private final static String redisTokenKeyFormat = REDIS_AUTHORIZATION_KEY + ":%s.tokenValue:%s";

    private final RegisteredClientRepository registeredClientRepository;

    private final OAuth2AuthorizationGrantAuthorizationRepository authorizationGrantAuthorizationRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final static Class<AbstractOAuth2Token>[] OAuth2TokenClasses = new Class[]{
            OAuth2AccessToken.class,
            OAuth2AuthorizationCode.class,
            OAuth2RefreshToken.class,
            OAuth2DeviceCode.class,
            OAuth2UserCode.class,
            OidcIdToken.class
    };

    /**
     * Redis索引有效期(默认6小时)
     */
    @Setter
    private Duration redisIndexExpire = Duration.ofHours(6);

    /**
     * Redis中TokenValue的有效期
     * <p>
     * 首先会根据OAuth2颁发的token中expireAt字段计算token的有效期，优先以该值为准。
     * 在无法计算token有效期的情况下，才设置默认值(默认6小时)
     */
    @Setter
    private Duration redisTokenValueDefaultExpire = Duration.ofHours(6);

    /**
     * Spring data redis {@link org.springframework.data.repository.CrudRepository}
     * 通过 {@link org.springframework.data.redis.core.RedisHash} 写入数据时，会自动生成一个管理所有id的Set，
     * 以便可以进行{@link CrudRepository#findAll()}操作。
     * 但Spring data并没有针对这个Set进行过期策略管理(ttl 为 -1)，该Set中的id值会一直残留在redis中，无限增长。
     * <p>
     * 提供自定义策略方便管理该Set(默认为删除策略)
     */
    @Setter
    private Consumer<String> objectSetPolicy = this::deleteObjectSet;


    public RedisOAuth2AuthorizationService(RegisteredClientRepository registeredClientRepository,
                                           OAuth2AuthorizationGrantAuthorizationRepository authorizationGrantAuthorizationRepository,
                                           RedisTemplate<String, Object> redisTemplate) {
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        Assert.notNull(authorizationGrantAuthorizationRepository,
                "authorizationGrantAuthorizationRepository cannot be null");
        Assert.notNull(redisTemplate, "redisTemplate cannot be null");
        this.registeredClientRepository = registeredClientRepository;
        this.authorizationGrantAuthorizationRepository = authorizationGrantAuthorizationRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        OAuth2AuthorizationGrantAuthorization authorizationGrantAuthorization = ModelMapper
                .convertOAuth2AuthorizationGrantAuthorization(authorization);
        this.authorizationGrantAuthorizationRepository.save(authorizationGrantAuthorization);

        this.expireRedisIndex(authorization);
        this.objectSetPolicy.accept(REDIS_AUTHORIZATION_KEY);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        this.authorizationGrantAuthorizationRepository.deleteById(authorization.getId());
    }

    @Nullable
    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.authorizationGrantAuthorizationRepository.findById(id)
                .map(this::toOAuth2Authorization)
                .orElse(null);
    }

    @Nullable
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        OAuth2AuthorizationGrantAuthorization authorizationGrantAuthorization = null;
        if (tokenType == null) {
            authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                    .findByStateOrAuthorizationCode_TokenValue(token, token);
            if (authorizationGrantAuthorization == null) {
                authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                        .findByAccessToken_TokenValueOrRefreshToken_TokenValue(token, token);
            }
            if (authorizationGrantAuthorization == null) {
                authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                        .findByIdToken_TokenValue(token);
            }
            if (authorizationGrantAuthorization == null) {
                authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                        .findByDeviceStateOrDeviceCode_TokenValueOrUserCode_TokenValue(token, token, token);
            }
        }
        else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository.findByState(token);
            if (authorizationGrantAuthorization == null) {
                authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                        .findByDeviceState(token);
            }
        }
        else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                    .findByAuthorizationCode_TokenValue(token);
        }
        else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                    .findByAccessToken_TokenValue(token);
        }
        else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                    .findByIdToken_TokenValue(token);
        }
        else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                    .findByRefreshToken_TokenValue(token);
        }
        else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                    .findByUserCode_TokenValue(token);
        }
        else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
                    .findByDeviceCode_TokenValue(token);
        }
        return authorizationGrantAuthorization != null ? toOAuth2Authorization(authorizationGrantAuthorization) : null;
    }

    private OAuth2Authorization toOAuth2Authorization(
            OAuth2AuthorizationGrantAuthorization authorizationGrantAuthorization) {
        RegisteredClient registeredClient = this.registeredClientRepository
                .findById(authorizationGrantAuthorization.getRegisteredClientId());
        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient);
        ModelMapper.mapOAuth2AuthorizationGrantAuthorization(authorizationGrantAuthorization, builder);
        return builder.build();
    }


    /**
     * 针对Spring data Redis中自动创建的index设置过期时间, 避免redis索引残留
     */
    private void expireRedisIndex(OAuth2Authorization authorization){
        String authorizationId = authorization.getId();
        //  redis idx key格式 => oauth2_authorization:{authorizationId}:idx
        String redisIndexKey = REDIS_AUTHORIZATION_KEY + ":" + authorizationId + ":idx";

        redisTemplate.expire(redisIndexKey, redisIndexExpire);

        for (Class<AbstractOAuth2Token> oAuth2TokenClass : OAuth2TokenClasses) {
            OAuth2Authorization.Token<AbstractOAuth2Token> token = authorization.getToken(oAuth2TokenClass);
            if (Objects.isNull(token)) {
                continue;
            }
            String tokenValue = token.getToken().getTokenValue();
            Instant expiresAt = token.getToken().getExpiresAt();
            String redisTokenType = convertRedisTokenType(oAuth2TokenClass);

            //  redis tokenValue格式 => oauth2_authorization:{redisTokenType}.tokenValue:{tokenValue}
            String redisKey = String.format(redisTokenKeyFormat, redisTokenType, tokenValue);

            redisTemplate.expire(redisKey, calculateTtl(expiresAt));
        }
        //  todo: state 审批流程?

    }

    private Duration calculateTtl(Instant expiresAt) {
        return Objects.isNull(expiresAt) ? redisTokenValueDefaultExpire : Duration.between(Instant.now(), expiresAt);
    }

    private String convertRedisTokenType(Class<AbstractOAuth2Token> clazz) {
        if (OAuth2AccessToken.class.equals(clazz)) {
            return "accessToken";
        } else if (OAuth2AuthorizationCode.class.equals(clazz)) {
            return "authorizationCode";
        } else if (OAuth2RefreshToken.class.equals(clazz)) {
            return "refreshToken";
        } else if (OAuth2DeviceCode.class.equals(clazz)) {
            return "deviceCode";
        } else if (OAuth2UserCode.class.equals(clazz)) {
            return "userCode";
        } else if (OidcIdToken.class.equals(clazz)) {
            return "idToken";
        }

        return null;
    }

    /**
     * Spring data redis {@link org.springframework.data.repository.CrudRepository}
     * 通过 {@link org.springframework.data.redis.core.RedisHash} 写入数据时，会自动生成一个管理所有id的Set，以便可以进行findAll()的操作。
     * 但Spring data并没有针对这个Set进行过期策略管理(ttl 为 -1)，该Set中的id值会一直残留在redis中，无限增长。
     * 该策略在每次授权请求完成后，手动清理这个Set
     */
    private void deleteObjectSet(String key){
        redisTemplate.delete(key);
    }
}
