package com.capy.capyjara.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "重置密码DTO")
public class ResetPasswordDTO {

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "新密码(加密传输)")
    private String newPassword;
}
