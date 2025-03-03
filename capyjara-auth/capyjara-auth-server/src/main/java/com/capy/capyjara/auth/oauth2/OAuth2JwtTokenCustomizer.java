package com.capy.capyjara.auth.oauth2;

import cn.hutool.core.bean.copier.CopyOptions;
import com.capy.capyjara.auth.api.constant.AuthApiConstant;
import com.capy.capyjara.auth.config.AuthServerProperties;
import com.capy.capyjara.common.security.LoginUser;
import com.capy.capyjara.starter.MvcSecurityStarterProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;

/**
 * 自定义OAuth2Token生成器
 * <p>
 * 颁发JWT时，可根据自定义需求进行修改
 * <p>
 * OAuth2TokenCustomizer<JwtEncodingContext>自定义配置类 暂时只能通过@Bean @Component等注解直接注入IOC容器
 * 没有找到通过SecurityFilterChain修改配置的方法。
 * Spring Security启动时，会从IoC容器中找到该自定义配置，并自动配置到SecurityFilterChain中
 *
 * @see <a href="https://docs.spring.io/spring-authorization-server/reference/core-model-components.html#oauth2-token-customizer">Spring Doc: oauth2-token-customizer</a>
 * @see JwtGenerator
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2JwtTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final AuthServerProperties authServerProperties;

    private final MvcSecurityStarterProperties mvcSecurityStarterProperties;

    @Setter
    private CopyOptions authorizationCodePrincipalCopyOptions = CopyOptions.create().setIgnoreProperties(AuthApiConstant.JWT_CLAIM_ATTRIBUTES);


    @Override
    public void customize(JwtEncodingContext context) {
        String grantType = context.getAuthorizationGrantType().getValue();
        RegisteredClient registeredClient = context.getRegisteredClient();

        log.debug("OAuth2 颁发token RegisteredClientId:{}, grant_type:{}", registeredClient.getClientId(), grantType);
        if (AUTHORIZATION_CODE.getValue().equals(grantType)) {
            customizeAuthorizationCode(context);
        }
        else if (REFRESH_TOKEN.getValue().equals(grantType)) {
            customizeRefreshToken(context);
        }
        else if (PASSWORD.getValue().equals(grantType)) {
            customizePassword(context);
        }
        else if (CLIENT_CREDENTIALS.getValue().equals(grantType)) {
            customizeClientCredentials(context);
        }
        //
        else if (JWT_BEARER.getValue().equals(grantType)) {
            customizeJwtBearer(context);
        }
        else if (DEVICE_CODE.getValue().equals(grantType)) {
            customizeDeviceCode(context);
        }

        //  也可根据自定义grant_type进行补充


    }

    protected void customizeAuthorizationCode(JwtEncodingContext context) {
        //  可根据需求自定义jwt payload中的字段

        Authentication authentication = context.getPrincipal();
        if (Objects.nonNull(authentication) &&
                authentication.getPrincipal() instanceof LoginUser loginUser) {
            claimLoginUserAttributes(context, loginUser);
        }


    }

    protected void customizeRefreshToken(JwtEncodingContext context) {

    }

    protected void customizePassword(JwtEncodingContext context) {

    }

    protected void customizeClientCredentials(JwtEncodingContext context) {

    }

    protected void customizeJwtBearer(JwtEncodingContext context) {

    }

    protected void customizeDeviceCode(JwtEncodingContext context) {

    }


    /**
     * 将{@link LoginUser}各字段签发至Jwt attributes中
     */
    private void claimLoginUserAttributes(JwtEncodingContext context, LoginUser loginUser) {
        Map<String, Object> attributes = loginUser.getAttributes();
        context.getClaims().claim(AuthApiConstant.JWT_CLAIM_ATTRIBUTES, attributes);
    }

}
