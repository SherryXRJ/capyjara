package com.capy.capyjara.admin.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth.login.lock")
public class AuthLoginLockProperty {

    /**
     * 是否开启登录失败锁定
     */
    private Boolean enable;

    /**
     * 密码连续输错限制次数
     */
    private Integer failNumLimit;

    /**
     * 登录锁定时长(分钟)
     */
    private Integer minuets;

}
