package com.capy.capyjara.admin.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.capy.capyjara.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema
@Data
@EqualsAndHashCode(callSuper=true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_tenant")
public class SysTenant extends BaseEntity {
    /**
     * 租户id
     */
    @TableId(value = "id", type = IdType.NONE)
    @Schema(description="租户id")
    private String id;

    /**
     * 租户名称
     */
    @TableField(value = "tenant_name")
    @Schema(description="租户名称")
    private String tenantName;

    /**
     * 启用状态(字典)
     */
    @TableField(value = "enabled")
    @Schema(description="启用状态(字典)")
    private String enabled;

    /**
     * 租户类型(字典)
     */
    @TableField(value = "tenant_type")
    @Schema(description="租户类型(字典)")
    private String tenantType;
}