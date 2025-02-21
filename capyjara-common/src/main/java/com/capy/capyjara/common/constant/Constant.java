package com.capy.capyjara.common.constant;

import lombok.Data;
import lombok.Value;

@Data
public class Constant {

    /**
     * capyjara 中自定义配置类的前缀
     */
    public static final String CONFIG_PREFIX = "capyjara.";

    public static final String LOGIN_CACHE_KEY_PREFIX = "capyjara_BACKEND_LOGIN_KEY:";

    /**
     * bearer token存放至cookie中的name
     */
    public static final String BEARER_TOKEN_COOKIE_NAME = "accessToken";

}
