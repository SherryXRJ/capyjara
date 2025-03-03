package com.capy.capyjara.starter;

import com.capy.capyjara.common.constant.Constant;
import com.capy.capyjara.starter.security.InternalRequestAuthorizationManager;
import com.capy.capyjara.starter.security.converter.JwtLoginUserConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web模块自定义属性配置<br/>
 *
 * 抽取出自定义配置类，方便对自定义配置进行属性配置
 */
@ConfigurationProperties(prefix = Constant.CONFIG_PREFIX + "mvc-security")
@Getter
@Setter
public class MvcSecurityStarterProperties {

    /**
     * JWT payload中Principal的Key值 <br/>
     *
     * 认证时将刚该值作为Key 签发写入JWT payload中<br>
     * 请求时从JWT payload中读取该Key 解析并在Spring Security中设置认证信息
     *
     * @see JwtLoginUserConverter
     */
    private String jwtPrincipalKey = "attributes";


    /**
     * jwtDecoder RSA 公钥
     */
    private String jwtDecoderPublicKey;


    /**
     * URL白名单
     * (不需要认证鉴权, Spring Security直接放行)
     */
    private String[] whitelistUrl;

    /**
     * 内部HTTP请求 SecretKey
     *
     * @see InternalRequestAuthorizationManager
     */
    @Deprecated
    private String internalRequestSecretKey = "capyjara-Internal-Secret-Key";

    private Boolean enableOAuth2Client = true;
}
