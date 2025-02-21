package com.capy.capyjara.auth.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(name = "用户VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {

    @Schema(description = "用户id")
    private Integer id;

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "显示名称")
    private String screenName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "是否启用(1.启用 0.禁用)")
    private Boolean enabled;

    @Schema(description = "锁定状态(0.未锁定 1.锁定)")
    private Boolean locked;

    @Schema(description = "用户类型(1.管理端用户 2.租户端用户)")
    private Integer type;

    @Schema(description = "租户id")
    private Integer tenantId;

    @Schema(description = "省编码")
    private String provinceCode;

    @Schema(description = "省名称")
    private String provinceName;

    @Schema(description = "市编码")
    private String cityCode;

    @Schema(description = "市名称")
    private String cityName;

    @Schema(description = "区编码")
    private String areaCode;

    @Schema(description = "区名称")
    private String areaName;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户类型(1.教育局 2.学校 3.省市公司)")
    private Integer tenantType;



}
