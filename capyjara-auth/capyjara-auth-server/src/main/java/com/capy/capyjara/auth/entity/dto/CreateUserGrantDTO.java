package com.capy.capyjara.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "创建用户授权DTO")
public class CreateUserGrantDTO {

    @Schema(description = "用户id")
    @NotNull
    private Integer userId;

    @Schema(description = "账号")
    @NotNull
    private String username;

    @Schema(description = "应用id")
    @NotNull
    private Integer applicationId;

    @Schema(description = "三方应用账号")
    @NotNull
    private String thirdAccount;

    @Schema(description = "租户id")
    @NotNull
    private Integer tenantId;

    @Schema(description = "应用client-id")
    @NotNull
    private String clientId;

}
