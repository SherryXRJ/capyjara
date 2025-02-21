package com.capy.capyjara.auth.controller;

import com.capy.capyjara.auth.config.AuthSecurityConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.oidc.web.OidcUserInfoEndpointFilter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "认证授权相关接口")
@RestController
@Slf4j
public class AuthController {

    /**
     * <p>
     * 在Spring Security OAuth2Client oauth2login流程中，授权服务必须提供userinfo uri以便框架可通过access_token解析当前登录的oauth2user基础信息。
     * 如果没有userinfo uri，Spring Security OAuth2Client则无法完成最后的{@link SecurityContextHolder#setContext(SecurityContext)}步骤
     * </p>
     * <p>
     * 为了支持gateway oauth2login，自定义一个专门用于oauth2授权场景获取用户基础信息的userinfo uri
     * </p>
     * <p>
     * 注意:这个接口不适用于oidc场景。因为该接口没有提供对应的idToken解析方式。
     * 我们在OAuth2授权服务中已经启用了oidc {@link AuthSecurityConfig#oAuth2authorizationServerSecurityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity)}
     * Spring Security Authorization Server会自动生成一个 "/userinfo" 接口 {@link OidcUserInfoEndpointFilter}
     * 因此，oidc场景下oauth2login场景下需要使用"/userinfo"这个接口
     * </p>
     *
     * @see org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService#getUserNameAttributeName(org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest)
     * @see org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService#loadUser(org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest)
     * @see org.springframework.security.config.oauth2.client.CommonOAuth2Provider
     */
    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal Jwt jwt) {
        log.debug("oauth2-userinfo endpoint receive: {}", jwt.getClaims());

        return jwt.getClaims();
    }

}
