package com.capy.capyjara.admin.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用表
 */

@Schema(description = "应用表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_application")
public class Application {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键")
    @NotNull(message = "主键不能为null")
    private Integer id;

    /**
     * 应用名
     */
    @TableField(value = "`name`")
    @Schema(description = "应用名")
    @Size(max = 16, message = "应用名最大长度要小于 16")
    @NotBlank(message = "应用名不能为空")
    private String name;

    /**
     * 应用编码
     */
    @TableField(value = "code")
    @Schema(description = "应用编码")
    @Size(max = 16, message = "应用编码最大长度要小于 16")
    private String code;

    /**
     * 应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)
     */
    @TableField(value = "`type`")
    @Schema(description = "应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)")
    @NotNull(message = "应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)不能为null")
    private Integer type;

    /**
     * 接入类型(1.普通接入 2.OAuth2接入)
     */
    @TableField(value = "access_method")
    @Schema(description = "接入类型(1.普通接入 2.OAuth2接入)")
    @NotNull(message = "接入类型(1.普通接入 2.OAuth2接入)不能为null")
    private Integer accessMethod;

    /**
     * 厂商名称
     */
    @TableField(value = "vendor")
    @Schema(description = "厂商名称")
    @Size(max = 16, message = "厂商名称最大长度要小于 16")
    private String vendor;

    /**
     * 负责人姓名
     */
    @TableField(value = "manager",updateStrategy = FieldStrategy.IGNORED)
    @Schema(description = "负责人姓名")
    @Size(max = 16, message = "负责人姓名最大长度要小于 16")
    private String manager;

    /**
     * 负责人电话
     */
    @TableField(value = "manager_tel",updateStrategy = FieldStrategy.IGNORED)
    @Schema(description = "负责人电话")
    @Size(max = 16, message = "负责人电话最大长度要小于 16")
    private String managerTel;

    /**
     * 是否启用(1.启用 0.禁用)
     */
    @TableField(value = "enabled")
    @Schema(description = "是否启用(1.启用 0.禁用)")
    @NotNull(message = "是否启用(1.启用 0.禁用)不能为null")
    private Boolean enabled;

    /**
     * 访问地址
     */
    @TableField(value = "website",updateStrategy = FieldStrategy.IGNORED)
    @Schema(description = "访问地址")
    @Size(max = 512, message = "访问地址最大长度要小于 512")
    private String website;

    /**
     * 租户名
     */
    @TableField(value = "description")
    @Schema(description = "租户名")
    @Size(max = 256, message = "租户名最大长度要小于 256")
    private String description;

    /**
     * logo
     */
    @TableField(value = "logo")
    @Schema(description = "logo")
    @Size(max = 64, message = "logo最大长度要小于 64")
    private String logo;

    /**
     * logo下载地址
     */
    @TableField(value = "logo_url")
    @Schema(description = "logo下载地址")
    @Size(max = 512, message = "logo下载地址最大长度要小于 512")
    private String logoUrl;

    /**
     * client_id
     */
    @TableField(value = "client_id")
    @Schema(description = "client_id")
    @Size(max = 64, message = "client_id最大长度要小于 64")
    private String clientId;

    /**
     * client_secret
     */
    @TableField(value = "client_secret")
    @Schema(description = "client_secret")
    @Size(max = 64, message = "client_secret最大长度要小于 64")
    private String clientSecret;

    /**
     * redirect_url
     */
    @TableField(value = "redirect_url",updateStrategy = FieldStrategy.IGNORED)
    @Schema(description = "redirect_url")
    @Size(max = 512, message = "redirect_url最大长度要小于 512")
    private String redirectUrl;

    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private Integer createUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    @NotNull(message = "创建时间不能为null")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改人")
    private Integer updateUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    @NotNull(message = "修改时间不能为null")
    private Date updateTime;

    /**
     * 逻辑删除(0.未删除 1.删除)
     */
    @TableField(value = "deleted")
    @Schema(description = "逻辑删除(0.未删除 1.删除)")
    @NotNull(message = "逻辑删除(0.未删除 1.删除)不能为null")
    @TableLogic(value = "0", delval = "1")
    private Boolean deleted;
}