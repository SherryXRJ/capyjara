package com.capy.capyjara.gateway.config.oauth2.bfseparation;

import com.capy.capyjara.common.constant.Constant;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

/**
 * 前后端分离模式下的OAuth2login SuccessHandler
 * 将oauth2 access_token等信息写入Cookie
 * 登录成功后重定向到指定页面
 *
 */
@Slf4j
public class CookieAuthenticationSuccessHandler extends RedirectServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final static String COOKIE_NAME = Constant.BEARER_TOKEN_COOKIE_NAME;

    @Setter
    private ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService;

    public CookieAuthenticationSuccessHandler() {
        super();
    }

    public CookieAuthenticationSuccessHandler(String location) {
        super(location);
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            return reactiveOAuth2AuthorizedClientService
                    .loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getPrincipal().getName())
                    .map(auth -> auth.getAccessToken().getTokenValue())
                    .doOnSuccess(token -> response.addCookie(buildAccessTokenCookie(token)))
                    .then(super.onAuthenticationSuccess(webFilterExchange, authentication));
        }
        return super.onAuthenticationSuccess(webFilterExchange, authentication);
    }


    /**
     * Cookie安全策略
     * <p>
     * httpOnly => true ;   禁止客户端js访问/修改;   防止XSS攻击
     * SameSite => Strict;  禁止跨域访问携带;        CSRF防护
     */
    protected ResponseCookie buildAccessTokenCookie(String accessToken){
        return ResponseCookie.from(COOKIE_NAME, accessToken)
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                .path("/")
                .build();
    }

}
