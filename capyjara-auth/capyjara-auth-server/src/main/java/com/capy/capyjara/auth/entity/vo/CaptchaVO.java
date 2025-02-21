package com.capy.capyjara.auth.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "图形验证码VO")
public class CaptchaVO {

    @Schema(description = "验证码id")
    private String captchaId;

    @Schema(description = "进行Base64编码的图形验证码")
    private String captcha;
}
