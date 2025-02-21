package com.capy.capyjara.auth.entity.dto;

import com.capy.capyjara.common.entity.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "查询用户授权列表")
public class QueryUserGrantListDTO extends PageQueryDTO implements Serializable {

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户id不能为空")
    private Integer userId;

    @Schema(description = "三方账号")
    private String thirdAccount;

    @Schema(description = "应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)")
    private Integer appType;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "client-id")
    private String clientId;

}
