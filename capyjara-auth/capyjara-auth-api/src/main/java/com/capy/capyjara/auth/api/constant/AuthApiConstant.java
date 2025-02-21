package com.capy.capyjara.auth.api.constant;

/**
 * 认证模块-常量
 */
public interface AuthApiConstant {

    /**
     * 管理端 OAuth2 client_id
     */
    String OAUTH2_ADMIN_CLIENT_ID = "capyjara-admin";

    /**
     * 门户端 OAuth2 client_id
     */
    String OAUTH2_PORTAL_CLIENT_ID = "capyjara-portal";

    /**
     * RSA Key id
     */
    String RSA_KEY_ID = "capyjara";

    /**
     * jwt 自定义claim 用户id
     */
    String JWT_CLAIM_USERID = "userId";

    /**
     * jwt 自定义claim 角色
     */
    String JWT_CLAIM_ROLES = "roles";

    /**
     * jwt 自定义claim 账号
     */
    String JWT_CLAIM_USERNAME = "username";

    /**
     * jwt 自定义claim 是否是管理员
     */
    String JWT_CLAIM_ADMIN = "admin";

    /**
     * jwt 自定义claim 是否被锁定
     */
    String JWT_CLAIM_LOCKED = "locked";

    /**
     * jwt 自定义claim 是否启用
     */
    String JWT_CLAIM_ENABLE = "enable";

    /**
     * jwt 自定义claim attributes
     */
    String JWT_CLAIM_ATTRIBUTES = "attributes";

    /**
     * jwt 自定义claim attributes-第三方应用账号
     * attributes.thirdAccount
     */
    String JWT_CLAIM_ATTRIBUTES_THIRD_ACCOUNT = "thirdAccount";

    /**
     * jwt 自定义claim attributes-租户id
     * attributes.tenantId
     */
    String JWT_CLAIM_ATTRIBUTES_TENANT_ID = "tenantId";

    /**
     * jwt 自定义claim attributes-租户名称
     * attributes.tenantName
     */
    String JWT_CLAIM_ATTRIBUTES_TENANT_NAME = "tenantName";

    /**
     * jwt 自定义claim attributes-用户类型
     * attributes.type
     */
    String JWT_CLAIM_ATTRIBUTES_TYPE = "type";

}
