package com.capy.capyjara.auth.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "用户授权VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGrantVO {

    @Schema(description = "授权id")
    private Integer id;

    @Schema(description = "应用id")
    private Integer applicationId;

    @Schema(description = "应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)")
    private Integer applicationType;

    @Schema(description = "应用名称")
    private String applicationName;

    @Schema(description = "三方账号")
    private String thirdAccount;

    @Schema(description = "client-id")
    private String clientId;

}
