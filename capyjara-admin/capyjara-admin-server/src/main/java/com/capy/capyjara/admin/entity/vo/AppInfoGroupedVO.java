package com.capy.capyjara.admin.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AppInfoGroupedVO {

    private AppTypeVO type;

    private List<AppInfoVO> list;
}
