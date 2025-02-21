package com.capy.capyjara.starter.web.security.converter;

import com.capy.capyjara.common.exception.BusinessException;
import com.capy.capyjara.common.response.CommonResultStatus;
import com.capy.capyjara.common.security.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;

import java.util.Collection;
import java.util.Objects;

/**
 * JWT token转换器(解析顺序: Jwt -> Redis -> jvm内存) <br/>
 * Spring Security Resource Server配置中 可将请求中的JWT-token转换为Authentication(LoginUser)
 *
 * 用户权限信息较为敏感，且api权限较多时，如果将权限信息全部写入jwt payload中会导致jwt过大，
 * 因此jwt只签发用户基础信息，通过redis进一步解析登录用户的详情
 *
 * @see JwtBearerTokenAuthenticationConverter
 * @see com.capy.capyjara.common.security.CurrentOAuth2User
 */
@Slf4j
public class JwtRedisLoginUserConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtBearerTokenAuthenticationConverter bearerTokenAuthenticationConverter = new JwtBearerTokenAuthenticationConverter();

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        AbstractAuthenticationToken bearerToken = bearerTokenAuthenticationConverter.convert(jwt);
        log.debug("OAuth2 Resource Server 开始解析jwt bearerToken:{}", bearerToken);

        String name = bearerToken.getName();
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(name);

        if (Objects.isNull(loginUser)) {
            log.debug("OAuth2 Resource Server 没有从Redis中获取到用户登录信息");
            throw new BusinessException(CommonResultStatus.UNAUTHORIZED);
        }
        log.debug("OAuth2 Resource Server 从Redis中获取到用户信息:{}", loginUser);

        Collection<GrantedAuthority> authorities = bearerToken.getAuthorities();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt());
        return new BearerTokenAuthentication(loginUser, accessToken, authorities);
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
