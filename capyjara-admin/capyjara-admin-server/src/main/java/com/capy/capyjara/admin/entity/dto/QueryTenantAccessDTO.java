package com.capy.capyjara.admin.entity.dto;


import com.capy.capyjara.common.entity.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "搜索应用关联租户DTO")
public class QueryTenantAccessDTO extends PageQueryDTO {

    /**
     * 主键
     */
    @Schema(description="应用Id")
    @NotNull(message = "主键不能为null")
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
     * 启用状态(0.禁用 1.启用)
     */
    @Schema(description="启用状态(0.禁用 1.启用)")
    private Boolean enabled;
}
