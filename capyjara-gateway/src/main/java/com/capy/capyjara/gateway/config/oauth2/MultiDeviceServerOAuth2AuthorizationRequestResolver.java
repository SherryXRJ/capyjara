package com.capy.capyjara.gateway.config.oauth2;

import cn.hutool.core.util.IdUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Function;

/**
 * 支持OAuth2多设备多端登录
 *
 * 在执行oauth2login流程前 生成deviceId并写入cookie;
 * {@link ReactiveRedisOAuth2AuthorizedClientService} 执行oauth2授权码授权流程时，将deviceId作为当前设备端的key写入redis
 * Spring Security执行完oauth2login流程后 将access_token refresh_token等信息作为value写入redis。这样不同device端 就可以绑定不同的access_token refresh_token
 *
 * 在BFF架构中，access_token与refresh_token都由后端存储。
 * 前后端分离模式下，前后端由session与cookie保持登录状态。
 * 前端每次请求后端接口时，由Spring Gateway代理获取当前用户的access_token信息，并基于TokenRelay策略 携带token将请求转发到各个Resource Server。
 * tips: 为什么不使用sessionId，而需要生成一个自定义的deviceId呢?
 *
 * 在执行oauth2login流程前，Spring会生成一个sessionId。
 * Spring Security在默认的会话管理策略中，为了防止Session Fixation(会话固定攻击)，在登录成功后会刷新sessionId。
 * 这种情况下因为sessionId发生变化，因此无法基于sessionId绑定多端的设备
 *
 * @see ReactiveRedisOAuth2AuthorizedClientService
 * @see org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService
 */
public class MultiDeviceServerOAuth2AuthorizationRequestResolver extends DefaultServerOAuth2AuthorizationRequestResolver implements ServerOAuth2AuthorizationRequestResolver {

    public final static String COOKIE_PREFIX = "deviceId";

    private Function<String, String> idGenerator = clientRegistration -> IdUtil.simpleUUID();

    private Duration maxCookieAge = Duration.ofSeconds(60);

    public MultiDeviceServerOAuth2AuthorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        super(clientRegistrationRepository);
    }

    public MultiDeviceServerOAuth2AuthorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository, ServerWebExchangeMatcher authorizationRequestMatcher) {
        super(clientRegistrationRepository, authorizationRequestMatcher);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
        writeCookie(exchange, clientRegistrationId);
        return super.resolve(exchange, clientRegistrationId);
    }

    public void setIdGenerator(Function<String, String> idGenerator) {
        Assert.notNull(idGenerator, "idGenerator is null");
        this.idGenerator = idGenerator;
    }

    public void setMaxCookieAge(Duration maxCookieAge) {
        Assert.notNull(idGenerator, "maxCookieAge is null");
        this.maxCookieAge = maxCookieAge;
    }

    protected void writeCookie(ServerWebExchange exchange, String clientRegistrationId){
        exchange.getResponse().addCookie(
                ResponseCookie
                        .from(COOKIE_PREFIX, idGenerator.apply(clientRegistrationId))
                        .path("/")
                        .httpOnly(true)
                        .maxAge(maxCookieAge)
                        .build()
        );
    }

}
