package com.capy.capyjara.starter.security.converter;

import cn.hutool.core.bean.BeanUtil;
import com.capy.capyjara.common.security.LoginUser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * JWT token转换器(解析顺序: Jwt -> Redis -> jvm内存) <br/>
 * Spring Security Resource Server配置中 可将请求中的JWT-token转换为Authentication(LoginUser)
 * <p>
 * 用户权限信息较为敏感，且api权限较多时，如果将权限信息全部写入jwt payload中会导致jwt过大，
 * 因此jwt只签发用户基础信息，通过redis进一步解析登录用户的详情
 *
 * @see JwtBearerTokenAuthenticationConverter
 * @see com.capy.capyjara.common.security.CurrentOAuth2User
 */
@Slf4j
public class JwtLoginUserConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtBearerTokenAuthenticationConverter bearerTokenAuthenticationConverter = new JwtBearerTokenAuthenticationConverter();

    @Setter
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        AbstractAuthenticationToken bearerToken = bearerTokenAuthenticationConverter.convert(jwt);

//        LoginUser loginUser = retrieveLoginUser(bearerToken);
        LoginUser loginUser = new LoginUser();
        Object object = jwt.getClaims().get("attributes");
        BeanUtil.copyProperties(object, loginUser);


        if (Objects.isNull(loginUser)) {
            log.debug("OAuth2 Resource Server 没有从Redis中获取到用户登录信息");
            return null;
        }

        log.debug("OAuth2 Resource Server 从Redis中获取到用户信息:{}", loginUser);

        //  todo: 1.读取角色 2.设置api权限
//        Collection<String> roles = loginUser.getRoles();

        Collection<GrantedAuthority> authorities = bearerToken.getAuthorities();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt());
        return new BearerTokenAuthentication(loginUser, accessToken, authorities);
    }

    protected LoginUser retrieveLoginUser(AbstractAuthenticationToken bearerToken) {
        log.debug("OAuth2 Resource Server 开始解析jwt bearerToken:{}", bearerToken);
        String name = bearerToken.getName();
        return StringUtils.hasText(name) ? (LoginUser) redisTemplate.opsForValue().get(name) : null;
    }

    protected Collection<String> selectApiPermissions(Collection<String> roles){
        //  todo: 从redis获取角色对应的api权限

        return Collections.emptyList();
    }

}
