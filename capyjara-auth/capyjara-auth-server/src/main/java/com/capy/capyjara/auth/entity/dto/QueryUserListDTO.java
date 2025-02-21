package com.capy.capyjara.auth.entity.dto;

import com.capy.capyjara.common.entity.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "查询用户列表")
public class QueryUserListDTO extends PageQueryDTO implements Serializable {

    @Schema(description = "账号")
    private String username;

    @Schema(description = "用户名")
    private String screenName;

    @Schema(description = "是否启用(1.启用 0.禁用)")
    private Boolean enabled;

    @Schema(description = "锁定状态(0.未锁定 1.锁定)")
    private Boolean locked;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "用户类型(1.管理端用户 2.租户端用户)", defaultValue = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer type;

    @Schema(description = "租户类型(1.教育局 2.学校 3.省市公司)")
    private Integer tenantType;
}
