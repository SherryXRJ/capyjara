package com.capy.capyjara.auth.api.util;

import com.capy.capyjara.common.security.CurrentOAuth2User;
import com.capy.capyjara.common.security.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /**
     * 获取当前认证用户
     *
     * @return 登录认证后的用户
     * @see CurrentOAuth2User
     */
    public static LoginUser getLoginUser(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof LoginUser ?
                (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
    }
}
