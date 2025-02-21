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
@Schema(name = "修改用户授权DTO")
public class UpdateUserGrantDTO {

    @Schema(description = "授权id")
    private Integer id;

    @Schema(description = "三方应用账号")
    @NotNull
    private String thirdAccount;

}
