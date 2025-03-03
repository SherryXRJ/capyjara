package com.capy.capyjara.admin.controller;

import com.capy.capyjara.common.response.Result;
import com.capy.capyjara.common.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController(value = "/area")
@Slf4j
@Tag(name = "【基础服务】地址服务接口", description = "获取地址列表")
public class AreaController {


    @Resource
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;


    @GetMapping("/provinceList")
    @Operation(summary = "获取全部省份")
    public Result provinceList(HttpServletRequest request) {
        System.out.println(request.getHeader("Authorization"));
        return Result.ok(SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping("/no-auth")
    public Result provinceList() {
        OAuth2AuthorizedClient capyId = oAuth2AuthorizedClientManager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("capyId")
                        .principal("test")
                .build()
        );
        String tokenValue = capyId.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        System.out.println(tokenValue);
        headers.set("Authorization", "Bearer " + tokenValue);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = new RestTemplate().exchange(
                "http://localhost:8080/auth/resourceTest",
                HttpMethod.GET,
                entity,
                String.class);
        return Result.ok(response.getBody());
    }

}
