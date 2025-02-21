package com.capy.capyjara.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "添加用户DTO")
public class AddUserDTO implements Serializable {

    @Schema(description = "用户类型(1.管理端用户 2.租户端用户)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Range(min = 1, max = 2)
    private Integer type;

    @Schema(description = "账号")
    @NotNull
    private String username;

    @Schema(description = "姓名")
    @NotNull(message = "请输入姓名")
    @Size(min = 2, max = 16, message = "姓名长度有效范围2~16")
    private String screenName;

    @Schema(description = "电话号码")
    @Pattern(regexp = "^[0-9]{0,11}$")
    private String phone;

    @Schema(description = "加密之后的密码")
    @NotNull
    private String password;

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

    @Schema(description = "租户类型(1.教育局 2.学校 3.省市公司)")
    private Integer tenantType;
}
