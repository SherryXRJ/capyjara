package com.capy.capyjara.admin.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "接入应用租户信息")
public class AppTenantInfoVo {

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
     * 租户名
     */
    @Schema(description="租户名")
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
}
