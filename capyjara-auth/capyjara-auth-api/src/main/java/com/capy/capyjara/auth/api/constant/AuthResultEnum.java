package com.capy.capyjara.auth.api.constant;

import com.capy.capyjara.common.response.ResultStatus;
import lombok.Getter;

@Getter
public enum AuthResultEnum implements ResultStatus {


    LOGIN_FAIL(100000001, "登录失败"),
    NOT_FOUND_USER(100000002, "未找到用户"),
    NOT_FOUND_TENANT(100000003, "未找到租户"),

    ;

    private final Integer code;

    private final String msg;


    AuthResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
