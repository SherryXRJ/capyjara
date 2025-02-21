package com.capy.capyjara.admin.auth.config.convert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.extern.slf4j.Slf4j;
import com.capy.capyjara.admin.entity.LoginAdminUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
@Slf4j
public class JwtLoginAdminUserConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();

        //  todo: 用户权限信息存放至redis，这里从redis读取与转换
        //  fixme: 如果限制用户用户同时在线数量，如何设置？在登录时？
        Map<String, Object> attributes = jwt.getClaimAsMap("attributes");
        LoginAdminUser loginUser = BeanUtil.toBean(attributes, LoginAdminUser.class, CopyOptions.create());

        return UsernamePasswordAuthenticationToken.authenticated(loginUser, null, loginUser.getAuthorities());
    }

}
