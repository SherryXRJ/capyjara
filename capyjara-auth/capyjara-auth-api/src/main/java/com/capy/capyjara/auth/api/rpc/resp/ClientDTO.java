package com.capy.capyjara.auth.api.rpc.resp;

import lombok.Data;

@Data
public class ClientDTO {

    /**
     * 应用名
     */

    private String name;

    /**
     * 应用编码
     */

    private String code;

    /**
     * 应用类型(1.教育教学 2.教育管理 3.教育评价 4生活服务 5.平安校园)
     */

    private Integer type;

    /**
     * 接入类型(1.普通接入 2.OAuth2接入)
     */

    private Integer accessMethod;

    /**
     * 厂商名称
     */

    private String vendor;

    /**
     * 负责人姓名
     */

    private String manager;

    /**
     * 负责人电话
     */
    private String managerTel;


    /**
     * 访问地址
     */
    private String website;

    /**
     * 应用描述
     */
    private String description;

    /**
     * client_id
     */
    private String clientId;

    /**
     * client_secret
     */
    private String clientSecret;

    /**
     * redirect_url
     */
    private String redirectUrl;
}
