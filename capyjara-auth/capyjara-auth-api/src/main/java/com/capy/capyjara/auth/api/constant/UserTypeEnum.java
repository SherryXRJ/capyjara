package com.capy.capyjara.auth.api.constant;

public enum UserTypeEnum {
    MANAGEMENT(1, "管理端用户"),
    TENANT(2, "租户端用户");

    private final Integer code;

    private final String name;


    UserTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
