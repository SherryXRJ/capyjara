package com.capy.capyjara.auth.controller;

import com.capy.capyjara.auth.service.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.capy.capyjara.auth.entity.vo.CaptchaVO;
import com.capy.capyjara.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Validated
@Tag(name = "验证码相关接口")
@RestController
@RequestMapping("/captcha/")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    @Operation(summary = "获取验证码")
    @GetMapping("/get")
    public Result<CaptchaVO> get(){
        return Result.ok(captchaService.generateCaptcha());
    }
}
