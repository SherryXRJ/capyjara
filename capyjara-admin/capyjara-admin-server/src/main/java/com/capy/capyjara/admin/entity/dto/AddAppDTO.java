package com.capy.capyjara.admin.entity.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;


@Schema(description="创建应用DTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddAppDTO implements Serializable {


    /**
     * 应用名
     */
    @Schema(description="应用名")
    @Size(max = 16,message = "应用名最大长度要小于 16")
    @NotBlank(message = "应用名不能为空")
    private String name;

    /**
     * 应用编码
     */
    @Schema(description="应用编码")
    @Size(max = 16,message = "应用编码最大长度要小于 16")
    private String code;

    /**
     * 应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)
     */
    @Schema(description="应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)")
    @NotNull(message = "应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)不能为null")
    private Integer type;

    /**
     * 接入类型(1.普通接入 2.OAuth2接入)
     */
    @Schema(description="接入类型(1.普通接入 2.OAuth2接入)")
    @NotNull(message = "接入类型(1.普通接入 2.OAuth2接入)不能为null")
    private Integer accessMethod;

    /**
     * 厂商名称
     */
    @Schema(description="厂商名称")
    @Size(max = 16,message = "厂商名称最大长度要小于 16")
    private String vendor;

    /**
     * 负责人姓名
     */
    @Schema(description="负责人姓名")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z]{0,8}$",message = "联系人姓名格式错误")
    private String manager;

    /**
     * 负责人电话
     */
    @Schema(description="负责人电话")
    @Pattern(regexp = "^[0-9]{0,11}$")
    private String managerTel;

    /**
     * 是否启用(1.启用 0.禁用)
     */
    @Schema(description="是否启用(1.启用 0.禁用)")
    @NotNull(message = "是否启用(1.启用 0.禁用)不能为null")
    private Boolean enabled;

    /**
     * 访问地址
     */

    @Schema(description="访问地址")
    @Size(max = 512,message = "访问地址最大长度要小于 256")
    private String website;

    /**
     * 应用描述
     */

    @Schema(description="应用描述")
    @Size(max = 256,message = "应用描述最大长度要小于 256")
    private String description;

    /**
     * logo
     */

    @Schema(description="logo")
    @Size(max = 64,message = "logo最大长度要小于 64")
    private String logo;

    /**
     * logo下载地址
     */
    @Schema(description = "logo下载地址")
    @Size(max = 512, message = "logo下载地址最大长度要小于 512")
    private String logoUrl;

    /**
     * redirect_url
     */
    @Schema(description="redirect_url")
    @Size(max = 512,message = "redirect_url最大长度要小于 512")
    private String redirectUrl;

}
