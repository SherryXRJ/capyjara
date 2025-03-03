package com.capy.capyjara.starter.security.converter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * BearerToken委托解析器
 * <p>
 * 按List顺序依次解析查找BearerToken
 */
public class DelegateBearerTokenResolver implements BearerTokenResolver {

    private List<BearerTokenResolver> bearerTokenResolvers;

    public DelegateBearerTokenResolver(List<BearerTokenResolver> bearerTokenResolvers) {
        Assert.notEmpty(bearerTokenResolvers, "bearerTokenResolvers is empty.");
        this.bearerTokenResolvers = bearerTokenResolvers;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        for (BearerTokenResolver bearerTokenResolver : bearerTokenResolvers) {
            String bearerToken = bearerTokenResolver.resolve(request);

            if (!StringUtils.hasText(bearerToken)) {
                continue;
            }

            return bearerToken;
        }

        return null;
    }
}
