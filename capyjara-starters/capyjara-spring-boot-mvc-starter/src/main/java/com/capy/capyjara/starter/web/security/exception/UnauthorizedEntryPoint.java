package com.capy.capyjara.starter.web.security.exception;

import cn.hutool.json.JSONUtil;
import com.capy.capyjara.common.constant.Constant;
import com.capy.capyjara.common.response.CommonResultStatus;
import com.capy.capyjara.common.response.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.debug("UnauthorizedEntryPoint {}, {}",request, authException.getCause());
        Result<String> result = Result.fail(CommonResultStatus.UNAUTHORIZED);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JSONUtil.toJsonStr(result));
        response.getWriter().close();
    }
}
