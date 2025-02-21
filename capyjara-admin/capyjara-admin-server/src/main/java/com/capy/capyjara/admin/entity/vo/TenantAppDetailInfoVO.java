package com.capy.capyjara.admin.entity.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;


@Data
public class TenantAppDetailInfoVO {
    /**
     * 主键
     */
    @Schema(description = "主键")
    private Integer id;

    /**
     * 租户id
     */
    @Schema(description = "租户id")
    private Integer tenantId;

    /**
     * 应用id
     */
    @Schema(description = "应用id")
    private Integer applicationId;


    /**
     * 排序
     */
    @Schema(description = "排序")
    private Byte sort;

    /**
     * 应用名
     */
    @Schema(description="应用名")
    private String name;


    /**
     * 应用编码
     */
    @Schema(description="应用编码")
    private String code;

    /**
     * 应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)
     */
    @Schema(description="应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)")
    private Integer type;

    /**
     * 应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)
     */
    @Schema(description="应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)")
    private String typeName;

    /**
     * 接入类型(1.普通接入 2.OAuth2接入)
     */
    @Schema(description="接入类型(1.普通接入 2.OAuth2接入)")
    private Integer accessMethod;

    /**
     * 厂商名称
     */
    @Schema(description="厂商名称")
    private String vendor;

    /**
     * 负责人姓名
     */
    @Schema(description="负责人姓名")
    private String manager;

    /**
     * 负责人电话
     */
    @Schema(description="负责人电话")
    private String managerTel;


    /**
     * 访问地址
     */

    @Schema(description="访问地址")
    private String website;

    /**
     * 应用描述
     */

    @Schema(description="应用描述")
    private String description;

    /**
     * logo
     */
    @Schema(description = "logo")
    private String logo;

    @Schema(description = "logo下载地址")
    @Size(max = 512, message = "logo下载地址最大长度要小于 512")
    private String logoUrl;


    /**
     * redirect_url
     */
    @Schema(description="redirect_url")
    private String redirectUrl;

    private String clientId;


}
