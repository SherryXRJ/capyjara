package com.capy.capyjara.admin.entity.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.util.Date;

@Data
public class TenantAppInfoVO {
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
     * client-id
     */
    @Schema(description = "clientId")
    private String clientId;

    /**
     * 应用id
     */
    @Schema(description = "应用id")
    private Integer applicationId;

    /**
     * 是否启用(1.启用 0.禁用)
     */
    @Schema(description = "是否启用(1.启用 0.禁用)")
    private Byte enabled;

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
     * 应用名
     */
    @Schema(description="开通时间")
    private Date createTime;

    /**
     * 应用名
     */
    @Schema(description="应用类型名")
    private String typeName;
    /**
     * 应用名
     */
    @Schema(description="应用类型")
    private Integer type;

}
