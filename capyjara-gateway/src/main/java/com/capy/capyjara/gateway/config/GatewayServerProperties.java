package com.capy.capyjara.gateway.config;

import com.capy.capyjara.common.constant.Constant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = Constant.CONFIG_PREFIX + "gateway")
@Getter
@Setter
public class GatewayServerProperties {

    /**
     * oauth2login最大会话数
     */
    private int maximumSessions = 10;

    /**
     * 认证模式
     * <p>
     * bff: BFF架构 - (由后端存储access_token refresh_token, 前后端通过session交互)
     * <p>
     * separation: 前后端分离模式 - (由前端存储管理access_token refresh_token。oauth2login授权完成后，后端将token返回给前端进行管理)
     */
    private String frontendBackendMode = "bff";

    /**
     * OAuth2Login成功后重定向地址
     */
    private String oAuth2LoginSuccessRedirectUri = "/";
}
