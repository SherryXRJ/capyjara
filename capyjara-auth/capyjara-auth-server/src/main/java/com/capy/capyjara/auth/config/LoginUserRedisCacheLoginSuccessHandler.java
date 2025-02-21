package com.capy.capyjara.auth.config;

import com.capy.capyjara.common.security.LoginUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功后，使用Redis存储当前登录用户信息
 *
 * jwt中仅颁发基础信息，其余数据，如权限信息，写入redis中
 *
 */
public class LoginUserRedisCacheLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedisTemplate<String, Object> redisTemplate;

    private Duration cacheTimeout = Duration.ofSeconds(1800);

    public LoginUserRedisCacheLoginSuccessHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public LoginUserRedisCacheLoginSuccessHandler(RedisTemplate<String, Object> redisTemplate, Duration cacheTimeout) {
        this.redisTemplate = redisTemplate;
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        Object principal = authentication.getPrincipal();
        if (principal.getClass().isAssignableFrom(LoginUser.class)) {
            LoginUser loginUser = (LoginUser) principal;
            String username = loginUser.getUsername();
            redisTemplate.opsForValue().set(username, loginUser, cacheTimeout);
        }

        super.onAuthenticationSuccess(request, response, authentication);

    }
}
