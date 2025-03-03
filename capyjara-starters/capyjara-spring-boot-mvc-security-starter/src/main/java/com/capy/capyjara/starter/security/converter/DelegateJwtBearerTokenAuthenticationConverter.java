package com.capy.capyjara.starter.security.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Slf4j
public class DelegateJwtBearerTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {


    private List<Converter<Jwt, AbstractAuthenticationToken>> converterList;

    public DelegateJwtBearerTokenAuthenticationConverter(List<Converter<Jwt, AbstractAuthenticationToken>> converterList) {
        Assert.notEmpty(converterList, "converterList must not be empty");
        this.converterList = converterList;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        for (Converter<Jwt, AbstractAuthenticationToken> jwtAbstractAuthenticationTokenConverter : converterList) {

            AbstractAuthenticationToken authenticationToken = null;
            try {
                authenticationToken = jwtAbstractAuthenticationTokenConverter.convert(source);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            if (Objects.nonNull(authenticationToken)) {
                return authenticationToken;
            }
        }

        return null;
    }
}
