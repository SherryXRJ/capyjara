package com.capy.capyjara.admin.controller;

import com.capy.capyjara.common.response.Result;
import com.capy.capyjara.common.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/area")
@Slf4j
@Tag(name = "【基础服务】地址服务接口", description = "获取地址列表")
public class AreaController {


    @GetMapping("/provinceList")
    @Operation(summary = "获取全部省份")
    public Result provinceList(HttpServletRequest request) {
        System.out.println(request.getHeader("Authorization"));
        return Result.ok(SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping("/")
    @Operation(summary = "获取全部省份")
    public Result provinceList(HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser) {
        System.out.println(request.getHeader("Authorization"));
        System.out.println(loginUser);
        return Result.ok(SecurityContextHolder.getContext().getAuthentication());
    }

}
