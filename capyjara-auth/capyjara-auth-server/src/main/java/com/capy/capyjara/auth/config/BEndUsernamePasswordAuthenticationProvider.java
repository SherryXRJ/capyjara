package com.capy.capyjara.auth.config;

import com.capy.capyjara.auth.api.constant.AuthResultEnum;
import com.capy.capyjara.common.exception.BusinessException;
import com.capy.capyjara.common.security.LoginUser;
import jakarta.security.auth.message.AuthStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * 账密登录方式 AuthenticationProvider
 */
public class BEndUsernamePasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    public BEndUsernamePasswordAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService){
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //  todo 密码校验
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //  基础信息
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (Objects.isNull(user) || !(user instanceof LoginUser)) {
            throw new BusinessException(AuthResultEnum.NOT_FOUND_USER);
        }

        //  租户信息

        //  角色菜单权限 api权限信息


        //  数据权限


        return user;
    }
}
