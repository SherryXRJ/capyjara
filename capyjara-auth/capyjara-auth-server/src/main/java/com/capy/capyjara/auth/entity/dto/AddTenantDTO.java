package com.capy.capyjara.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Data
@Schema(description = "创建租户实体" )
public class AddTenantDTO implements Serializable {
    /**
     * 租户名
     */
    @Schema(description="租户名")
    @NotBlank(message = "租户名不能为空")
    @Size(max = 32,message = "租户名长度不能超过32字符")
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
    @Pattern(regexp = "^[0-9A-Z]{4,8}$" ,message = "租户编码格式错误")
    @NotBlank
    private String code;


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
    @Schema(description="学段(0.幼儿园 1.小学 2.初中 3.高中 4.大学)")
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
    @NotBlank
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9]{0,128}$" ,message = "详细地址不能超过128字符")
    private String address;

    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z]{0,16}$",message = "联系人姓名不能超过16字符")
    private String contactName;

    /**
     * 联系人号码
     */
    @Schema(description = "联系人号码")
    @Pattern(regexp = "^[0-9]{0,11}$")
    private String contactPhone;
}
