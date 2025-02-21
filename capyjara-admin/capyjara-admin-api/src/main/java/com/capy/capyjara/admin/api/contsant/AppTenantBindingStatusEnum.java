package com.capy.capyjara.admin.api.contsant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppTenantBindingStatusEnum {

    BINDING((byte) 1,"绑定"),
    NOT_BINDING((byte) 0,"未绑定");

    private final byte code;

    private final String type;
}
