package com.capy.capyjara.admin.api.contsant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessMethodEnum {
    OAUTH2((byte) 2,"oauth2"),
    GENERAL((byte) 1,"普通接入");

    private final byte code;

    private final String type;


}
