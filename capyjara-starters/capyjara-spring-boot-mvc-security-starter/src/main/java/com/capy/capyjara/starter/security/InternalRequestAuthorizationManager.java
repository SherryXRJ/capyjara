package com.capy.capyjara.starter.security;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

/**
 * 内部HTTP请求AuthorizationManager <br/>
 *
 * 针对内部RPC接口(比如微服务之间需要通过Feign进行接口调用)，某些API不需要用户层面的认证授权
 * 如果直接放行存在一定安全隐患 authorizationManagerRequestMatcherRegistry.requestMatchers("/internal/**")permitAll()
 *
 * 因此需要内部约定一套额外的授权流程
 *
 * 检测调用方的请求头中携带的秘钥是否与配置的秘钥相同, 如果相同 则授权，如果不相同 则授权失败
 */
@Deprecated
public class InternalRequestAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    public final static String DEFAULT_INTERNAL_AUTH_HEADER = "X-Internal-Key";

    private final String secretKey;

    public InternalRequestAuthorizationManager(@NotNull String secretKey){
        this.secretKey = secretKey;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        String value = object.getRequest().getHeader(DEFAULT_INTERNAL_AUTH_HEADER);
        boolean isMatch = secretKey.equals(value);
        return new AuthorizationDecision(isMatch);
    }
}
