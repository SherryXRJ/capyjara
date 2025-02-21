package com.capy.capyjara.auth.oauth2;

import com.capy.capyjara.auth.config.AuthSecurityConfig;
import com.capy.capyjara.common.response.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Deprecated
public class CapyjaraTokenEndpointCustomizer implements Customizer<OAuth2TokenEndpointConfigurer> {

    private AuthenticationSuccessHandler accessTokenSuccessHandler;

    private AuthenticationFailureHandler errorResponseHandler;


    public CapyjaraTokenEndpointCustomizer(){
        this.accessTokenSuccessHandler = new OAuth2AccessTokenResponseHandler();
        this.errorResponseHandler = new OAuth2ErrorResponseHandler();
    }

    @Override
    public void customize(OAuth2TokenEndpointConfigurer oAuth2TokenEndpointConfigurer) {
//        oAuth2TokenEndpointConfigurer
//                .accessTokenResponseHandler(accessTokenSuccessHandler)
//                .errorResponseHandler(errorResponseHandler)
        ;
    }

    public void setAccessTokenSuccessHandler(AuthenticationSuccessHandler accessTokenSuccessHandler) {
        this.accessTokenSuccessHandler = accessTokenSuccessHandler;
    }

    public void setErrorResponseHandler(AuthenticationFailureHandler errorResponseHandler) {
        this.errorResponseHandler = errorResponseHandler;
    }

    static class OAuth2AccessTokenResponseHandler implements AuthenticationSuccessHandler{

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                    (OAuth2AccessTokenAuthenticationToken) authentication;

            OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
            OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
            Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

            OAuth2AccessTokenResponse.Builder builder =
                    OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                            .tokenType(accessToken.getTokenType())
                            .scopes(accessToken.getScopes());
            if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
                builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
            }
            if (refreshToken != null) {
                builder.refreshToken(refreshToken.getTokenValue());
            }
            if (!CollectionUtils.isEmpty(additionalParameters)) {
                builder.additionalParameters(additionalParameters);
            }
            OAuth2AccessTokenResponse accessTokenResponse = builder.build();
            AuthSecurityConfig.writeJsonResponse(response, Result.ok(accessTokenResponse));
        }
    }

    static class OAuth2ErrorResponseHandler implements AuthenticationFailureHandler{

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        }
    }


}
