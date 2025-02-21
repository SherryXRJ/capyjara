package com.capy.capyjara.admin.entity.dto;

import com.capy.capyjara.common.entity.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


import jakarta.validation.constraints.Size;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "搜索租户关联用户DTO")
public class QueryAppAccessDTO extends PageQueryDTO implements Serializable {

    @Schema(description="租户id")
    private Integer tenantId;

    /**
     * 应用名
     */
    @Schema(description="应用名")
    @Size(max = 16,message = "应用名最大长度要小于 16")
    private String name;

    /**
     * 应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)
     */
    @Schema(description="应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)")
    private Integer type;

    /**
     * 是否启用(1.启用 0.禁用)
     */
    @Schema(description="是否启用(1.启用 0.禁用)")
    private Boolean enabled;
}
