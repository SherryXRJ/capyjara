package com.capy.capyjara.admin.api.contsant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppTypeEnum {

    SAFE((byte) 1,"教育教学"),
    MANAGEMENT((byte)2,"教育管理"),
    TEACHING((byte)3,"教育评价"),
    EVALUATION((byte)4,"生活服务"),
    SERVICE((byte)5,"平安校园");

    private final byte code;

    private final String type;


    public static String getTypeByCode(byte byteValue) {
        for(AppTypeEnum appTypeEnum :AppTypeEnum.values()){
            if (appTypeEnum.getCode() == byteValue)
            return appTypeEnum.getType();
        }
        return null;
    }
}
