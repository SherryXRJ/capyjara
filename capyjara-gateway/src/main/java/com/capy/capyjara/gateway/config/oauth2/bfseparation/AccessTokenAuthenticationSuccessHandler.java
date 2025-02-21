package com.capy.capyjara.gateway.config.oauth2.bfseparation;

import cn.hutool.json.JSONUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 前后端分离模式下的OAuth2login SuccessHandler
 * 将oauth2 access_token等信息写入Response
 * <p>
 * 不推荐使用，建议使用OAuth2 PKCE模式
 */
@Deprecated
@Slf4j
public class AccessTokenAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Setter
    private ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            ServerHttpResponse httpResponse = webFilterExchange.getExchange().getResponse();
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            return reactiveOAuth2AuthorizedClientService
                    .loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getPrincipal().getName())
                    .map(this::retrieveTokenResponse)
                    .flatMap(resp -> {
                        String jsonStr = JSONUtil.toJsonStr(resp);
                        log.debug(AccessTokenAuthenticationSuccessHandler.class + " OAuth2Login Success write json to response, {}", jsonStr);

                        DataBufferFactory bufferFactory = httpResponse.bufferFactory();
                        DataBuffer buffer = bufferFactory.wrap(jsonStr.getBytes(StandardCharsets.UTF_8));
                        httpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        return httpResponse.writeWith(Mono.just(buffer));
                    });
        }
        return Mono.empty();
    }

    private TokenResponseEntity retrieveTokenResponse(OAuth2AuthorizedClient authorizedClient){
        OAuth2AccessToken oAuth2AccessToken = authorizedClient.getAccessToken();
        String accessToken = oAuth2AccessToken.getTokenValue();

        String name = authorizedClient.getPrincipalName();

        return TokenResponseEntity.builder()
                .name(name)
                .accessToken(accessToken)
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static final class TokenResponseEntity implements Serializable {

        private String name;

        private String accessToken;

    }

}
