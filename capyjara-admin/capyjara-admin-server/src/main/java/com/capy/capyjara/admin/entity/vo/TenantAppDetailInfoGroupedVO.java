package com.capy.capyjara.admin.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class TenantAppDetailInfoGroupedVO {

    private AppTypeVO type;


    private List<TenantAppDetailInfoVO> list;
}
