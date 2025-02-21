package com.capy.capyjara.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnableStatusEnum {

    /**
     * 状态
     */
    ENABLE(true,(byte) 1, "启用"),
    DISABLE(false,(byte) 0, "禁用");

    private final boolean aBoolean;

    private final byte code;

    private final String status;
}
