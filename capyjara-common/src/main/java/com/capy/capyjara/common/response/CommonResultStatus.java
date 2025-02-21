package com.capy.capyjara.common.response;

public enum CommonResultStatus implements ResultStatus{
    SUCCESS(200, "成功"),
    ILLEGAL_ARGUMENT(400, "参数异常"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403,"未授权" ),
    INTERNAL_ERROR(500, "内部异常"),
    SERVICE_UNAVAILABLE(503, "服务不可用")
    ;

    private final Integer code;

    private final String msg;

    CommonResultStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
