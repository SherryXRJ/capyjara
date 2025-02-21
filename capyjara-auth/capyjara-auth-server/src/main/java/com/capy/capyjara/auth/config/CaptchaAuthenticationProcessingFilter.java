package com.capy.capyjara.auth.config;

import com.capy.capyjara.auth.service.CaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * 验证码filter
 */
//@Component
public class CaptchaAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {

    private final static String PARAM_CAPTCHA_ID = "captchaId";

    private final static String PARAM_CAPTCHA_CODE = "captchaCode";

    /**
     * 是否开启验证码校验
     */
    @Value("${captcha.enabled:false}")
    private boolean captchaEnabled;

    @Resource
    private CaptchaService captchaService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (!requiresAuthentication(httpServletRequest, httpServletResponse) || !captchaEnabled) {
            chain.doFilter(request, response);
            return;
        }

        String captchaId = request.getParameter(PARAM_CAPTCHA_ID);
        String captchaCode = request.getParameter(PARAM_CAPTCHA_CODE);
        if (!StringUtils.hasText(captchaId) || !StringUtils.hasText(captchaCode)) {
            unsuccessfulAuthentication(httpServletRequest, httpServletResponse, new BadCredentialsException("请输入验证码"));
            return;
        }

        if (captchaService.isCaptchaExpired(captchaId)) {
            unsuccessfulAuthentication(httpServletRequest, httpServletResponse, new BadCredentialsException("验证码已过期"));
            return;
        }

        if (!captchaService.verifyCaptchaCode(captchaId, captchaCode)) {
            unsuccessfulAuthentication(httpServletRequest, httpServletResponse, new BadCredentialsException("验证码错误"));
            return;
        }

        chain.doFilter(request, response);
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    /* CaptchaFilter优先级高于 AbstractAuthenticationFilterConfigurer中的Filter 所以还需要单独配置SuccessHandler与FailureHandler */

    @Autowired
    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Autowired
    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }
}
