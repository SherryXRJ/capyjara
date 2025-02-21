package com.capy.capyjara.auth.config;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * 基于HTTP Session解析当前登录用户
 *
 * 与{@link com.capy.capyjara.common.security.CurrentOAuth2User}不同的是，一个基于Session(Redis Session)解析，另一个是基于JWT解析
 *
 * @see org.springframework.security.core.annotation.AuthenticationPrincipal
 * @see com.capy.capyjara.common.security.CurrentOAuth2User
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@AuthenticationPrincipal
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentSessionUser {
}