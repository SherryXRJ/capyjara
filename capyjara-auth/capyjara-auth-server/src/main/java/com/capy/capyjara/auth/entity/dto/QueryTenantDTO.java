package com.capy.capyjara.auth.entity.dto;


import com.capy.capyjara.common.entity.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "搜索租户实体")
public class QueryTenantDTO extends PageQueryDTO implements Serializable {

    @Schema(description="租户id")
    private Integer id;

    /**
     * 租户名
     */
    @Schema(description="租户名")
    @Size(max= 32)
    private String name;

    /**
     * 租户类型(1.教育局 2.学校 3.省市公司)
     */
    @Schema(description="租户类型(1.教育局 2.学校 3.省市公司)")
    private Integer type;

    /**
     * 租户编码
     */
    @Schema(description="租户编码")
    private String code;

    /**
     * 启用状态(0.禁用 1.启用)
     */
    @Schema(description="启用状态(0.禁用 1.启用)")
    private Boolean enabled;

    /**
     * 省编码
     */
    @Schema(description="省编码")
    private String provinceCode;

    /**
     * 省份名称
     */
    @Schema(description="省份名称")
    private String provinceName;

    /**
     * 市编码
     */
    @Schema(description="市编码")
    private String cityCode;

    /**
     * 市名称
     */
    @Schema(description="市名称")
    private String cityName;

    /**
     * 区编码
     */
    @Schema(description="区编码")
    private String areaCode;

    /**
     * 区名称
     */
    @Schema(description="区名称")
    private String areaName;

    /**
     * 学段(1.小学 2.初中 3.高中 4.大学)
     */
    @Schema(description="学段(1.小学 2.初中 3.高中 4.大学)")
    private Integer stage;

    /**
     * 租户级别(1.省 2.市 3.区)
     */
    @Schema(description="租户级别(1.省 2.市 3.区)")
    private Integer level;

    /**
     * 详细地址
     */
    @Schema(description="详细地址")
    private String address;

    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名")
    private String contactName;

    /**
     * 联系人号码
     */
    @Schema(description = "联系人号码")
    private String contactPhone;

}
