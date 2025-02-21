package com.capy.capyjara.auth.config;

import com.capy.capyjara.common.constant.Constant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ConfigurationProperties(prefix = Constant.CONFIG_PREFIX + "auth")
@Getter
@Setter
public class AuthServerProperties {

    /**
     * 同时在线Session数量配置
     */
    private Integer maximumSessions = 1;

    /**
     * bcrypt加密强度 4~31
     *
     * @see BCryptPasswordEncoder#BCryptPasswordEncoder(int)
     */
    private Integer bcryptLength = 10;

}
