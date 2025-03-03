package com.capy.capyjara.gateway.config.oauth2.bfseparation;

import com.capy.capyjara.common.constant.Constant;
import com.capy.capyjara.gateway.config.GatewayServerProperties;
import com.capy.capyjara.gateway.config.oauth2.MultiDeviceServerOAuth2AuthorizationRequestResolver;
import com.capy.capyjara.gateway.config.oauth2.ReactiveRedisOAuth2AuthorizedClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * 前后端分离模式下的Spring Security oauth2login配置
 * <p>
 * OAuth2认证成功后，将access_token写入Cookie中
 * <p>
 * <b>Note:</b>如果需要由前端管理access token，推荐使用OAuth2 PKCE(Auth-Server已支持PKCE模式) {@link com.capy.capyjara.auth.config.AuthSecurityConfig#clientRepository()}
 *
 * @see CookieAuthenticationSuccessHandler
 * @see com.capy.capyjara.starter.security.converter.CookieBearerTokenResolver
 * @see NoOpServerSecurityContextRepository
 * @see org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
 * @see <a href="https://docs.spring.io/spring-authorization-server/reference/1.2/guides/how-to-pkce.html">how-to-pkce</a>
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = Constant.CONFIG_PREFIX + "gateway", name = "frontendBackendMode", havingValue = "separation")
public class BfSeparationOAuth2LoginSecurityConfig {

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    private final ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    private final GatewayServerProperties gatewayServerProperties;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                //  前后端分离模式下禁用Session
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2Login(oAuth2LoginSpec -> oAuth2LoginSpec
                        //  oauth2login前置处理
                        .authorizationRequestResolver(serverOAuth2AuthorizationRequestResolver())
                        .authenticationSuccessHandler(bfSeparationAuthenticationSuccessHandler())
                )
                //  todo: 认证授权失败处理
//                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.accessDeniedHandler())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    public ServerOAuth2AuthorizationRequestResolver serverOAuth2AuthorizationRequestResolver(){
        MultiDeviceServerOAuth2AuthorizationRequestResolver auth2AuthorizationRequestResolver = new MultiDeviceServerOAuth2AuthorizationRequestResolver(reactiveClientRegistrationRepository);
        return auth2AuthorizationRequestResolver;
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService(){
        return new ReactiveRedisOAuth2AuthorizedClientService(reactiveRedisTemplate, reactiveClientRegistrationRepository);
    }

    @Bean
    public ServerAuthenticationSuccessHandler bfSeparationAuthenticationSuccessHandler(){
        CookieAuthenticationSuccessHandler successHandler = new CookieAuthenticationSuccessHandler(gatewayServerProperties.getOAuth2LoginSuccessRedirectUri());
        successHandler.setReactiveOAuth2AuthorizedClientService(reactiveOAuth2AuthorizedClientService());
        return successHandler;
    }

}
