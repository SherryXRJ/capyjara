package com.capy.capyjara.admin.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class AreaInfoVO {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 父类编码
     */
    private Integer parentId;

    /**
     * 区域编码
     */
    private Integer areaCode;

    /**
     * 级别 1 省 2 市 3 区县 4 街道等
     */
    private Integer areaLevel;

    /**
     * 名称
     */
    private String areaName;

    private String lon;

    private String lat;

    /**
     * 父类区域信息
     */
    private AreaInfoVO parentArea;


    private Boolean isDirectly;

    private String directlyCityCode;

    private Integer num = 0;

    private Integer openNum;

    private static final long serialVersionUID = 1L;

    public Boolean getDirectly() {
        return directlyValidate(areaCode);
    }

    public String getDirectlyCityCode() {
        if (getDirectly()) {
            return (areaCode + 100) + "";
        }
        return "";
    }


    private Boolean directlyValidate(Integer code) {
        List<Integer> directCity = new ArrayList<>();
        directCity.add(500000);
        directCity.add(110000);
        directCity.add(120000);
        directCity.add(310000);
        if (directCity.contains(code)) {
            return true;
        }
        return false;

    }
}
