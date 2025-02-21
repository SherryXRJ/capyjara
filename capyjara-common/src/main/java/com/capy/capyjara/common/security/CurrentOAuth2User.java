package com.capy.capyjara.common.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * 结合Spring MVC、Spring Security 以及JWT, 可通过注解获取当前登录用户
 *
 * @link {https://docs.spring.io/spring-security/reference/servlet/integrations/mvc.html#mvc-authentication-principal}
 *
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@AuthenticationPrincipal
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentOAuth2User {
}
