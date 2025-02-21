package com.capy.capyjara.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "修改密码DTO")
public class UpdatePasswordDTO {

    @Schema(description = "旧密码(加密传输)")
    @NotNull
    private String oldPassword;

    @Schema(description = "新密码(加密传输)")
    @NotNull
    private String newPassword;
}
