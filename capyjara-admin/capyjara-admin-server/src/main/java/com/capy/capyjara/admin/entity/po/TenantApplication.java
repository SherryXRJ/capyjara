package com.capy.capyjara.admin.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户应用关联表
 */
@Schema(description = "租户应用关联表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_tenant_application")
public class TenantApplication {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键")
    @NotNull(message = "主键不能为null")
    private Integer id;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    @Schema(description = "租户id")
    @NotNull(message = "租户id不能为null")
    private Integer tenantId;

    /**
     * 应用id
     */
    @TableField(value = "application_id")
    @Schema(description = "应用id")
    private Integer applicationId;

    /**
     * 是否启用(1.启用 0.禁用)
     */
    @TableField(value = "enabled")
    @Schema(description = "是否启用(1.启用 0.禁用)")
    @NotNull(message = "是否启用(1.启用 0.禁用)不能为null")
    private Byte enabled;

    /**
     * 排序
     */
    @TableField(value = "sort")
    @Schema(description = "排序")
    private Byte sort;

    /**
     * 创建人
     */
    @TableField(value = "create_user",fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private Integer createUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    @NotNull(message = "创建时间不能为null")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(value = "update_user" ,fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改人")
    private Integer updateUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time" ,fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    @NotNull(message = "修改时间不能为null")
    private Date updateTime;

    /**
     * 逻辑删除(0.未删除 1.删除)
     */
    @TableField(value = "deleted")
    @Schema(description = "逻辑删除(0.未删除 1.删除)")
    @TableLogic(value = "0",delval = "1")
    @NotNull(message = "逻辑删除(0.未删除 1.删除)不能为null")
    private Boolean deleted;
}