package com.capy.capyjara.auth.controller;

import com.capy.capyjara.auth.config.CurrentSessionUser;
import com.capy.capyjara.auth.service.CaptchaService;
import com.capy.capyjara.common.response.Result;
import com.capy.capyjara.common.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Validated
@RestController
public class TestController {

    @Resource
    private CaptchaService captchaService;

    @Resource
    private RedisTemplate redisTemplate;

    @Operation(summary = "")
    @GetMapping("/test/get")
    public Result<Object> get(@CurrentSessionUser LoginUser authentication){
        System.out.println(authentication);
        redisTemplate.opsForValue().set("test", LoginUser.builder().userId("123").password("pwd").build());

        return Result.ok(redisTemplate.opsForValue().get("test"));
    }

    @GetMapping("/test/oauth")
    public Result index(Model model,
//                        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                        @AuthenticationPrincipal OAuth2User oauth2User, HttpServletRequest request) {

        return Result.ok(oauth2User);
    }

    // todo 把这个接口放到开放接口中？
    @GetMapping("/user-info")
    public Object userinfo(@AuthenticationPrincipal Jwt jwt, HttpServletRequest request) {
        System.out.println(jwt);
        return jwt.getClaims();
    }

    @GetMapping("/resourceTest")
    public Object userinfo(@AuthenticationPrincipal OAuth2User authentication,HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);

        return authentication;
    }

    @PostMapping("/resourceTest")
    public Object resourceTest(@AuthenticationPrincipal Authentication authentication,HttpServletRequest request) {
        return authentication;
    }
}
