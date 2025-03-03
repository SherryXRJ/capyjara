package com.capy.capyjara.starter.security.converter;

import cn.hutool.core.util.ArrayUtil;
import com.capy.capyjara.common.constant.Constant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * 从Cookie中解析BearerToken
 *
 * @see DefaultBearerTokenResolver
 * @see <a href="https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/bearer-tokens.html">spring security bearer-tokens</a>
 */
public class CookieBearerTokenResolver implements BearerTokenResolver {

    private String cookieName = Constant.BEARER_TOKEN_COOKIE_NAME;

    public CookieBearerTokenResolver() {
    }

    public CookieBearerTokenResolver(String cookieName){
        Assert.hasText(cookieName, "cookieName is null");
        this.cookieName = cookieName;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtil.isEmpty(cookies)) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(c -> cookieName.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

    }
}
