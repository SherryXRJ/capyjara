package com.capy.capyjara.starter.web.security;

import com.capy.capyjara.starter.web.CapyjaraWebStarterProperty;
import com.capy.capyjara.starter.web.FeignConfig;
import com.capy.capyjara.starter.web.security.converter.CookieBearerTokenResolver;
import com.capy.capyjara.starter.web.security.converter.DelegateBearerTokenResolver;
import com.capy.capyjara.starter.web.security.converter.JwtRedisLoginUserConverter;
import com.capy.capyjara.starter.web.security.exception.ForbiddenEntryPoint;
import com.capy.capyjara.starter.web.security.exception.UnauthorizedEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.ObservationFilterChainDecorator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.ByteArrayInputStream;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Slf4j
@Configuration
@EnableConfigurationProperties(CapyjaraWebStarterProperty.class)
@RequiredArgsConstructor
public class WebSecurityMvcConfig {

    private final CapyjaraWebStarterProperty capyjaraWebStarterProperty;

    /**
     * Resource Server 过滤器链 <br/>
     *
     * 默认资源服务器过滤链配置, 将业务API交由Spring Security进行管理<br/>
     *
     * Spring Security内置的Filter
     * @see ObservationFilterChainDecorator.ObservationFilter#OBSERVATION_NAMES
     *
     * @see FeignConfig
     */
    @ConditionalOnMissingBean(name = "defaultResourceServerFilterChain")
    @Bean("defaultResourceServerFilterChain")
    @Order(0)
    public SecurityFilterChain defaultResourceServerFilterChain(HttpSecurity http, JwtDecoder jwtDecoder, @Qualifier("jwtConverter") Converter<Jwt, AbstractAuthenticationToken> jwtConverter) throws Exception {
        //  接口配置
        http
            .securityMatcher("/**")
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                authorizationManagerRequestMatcherRegistry
                    //  todo: 通过注解 Or oauth2 client credential?
                    .requestMatchers("/internal/**").access(new InternalRequestAuthorizationManager(capyjaraWebStarterProperty.getInternalRequestSecretKey()))
                    .requestMatchers(capyjaraWebStarterProperty.getWhitelistUrl()).permitAll()
                    //  其余接口需要认证鉴权
                    .anyRequest().authenticated();
        });


        http
            //  Resource Server相关配置
            .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                //  Jwt token解析器配置
                httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer ->
                        jwtConfigurer
                            .decoder(jwtDecoder)
                            .jwtAuthenticationConverter(jwtConverter)
                )
                    //  bearerToken解析器
                    .bearerTokenResolver(this.bearerTokenResolver())
                    //  未认证异常处理器
                    .authenticationEntryPoint(this.authenticationEntryPoint())
                    //  未授权异常处理器
                    .accessDeniedHandler(this.accessDeniedHandler())
                    //  todo: 多租户支持
//                    .authenticationManagerResolver()

            )
            //  Resource Server禁用Session
            .sessionManagement(httpSecuritySessionManagementConfigurer ->
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //  关闭csrf
            .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


    /**
     * Jwt token转换器(从Redis缓存中获取用户信息) <br/>
     *
     * 将Http请求中的Jwt token转换为Spring Security认证信息<br/>
     * (Spring Security OAuth2 Resource Server配置需要)
     */
    @ConditionalOnBean(name = "redisTemplate")
    @Bean("jwtConverter")
    public Converter<Jwt, AbstractAuthenticationToken> redisCacheJwtConverter(RedisTemplate<String, Object> redisTemplate){
        JwtRedisLoginUserConverter jwtRedisLoginUserConverter = new JwtRedisLoginUserConverter();
        jwtRedisLoginUserConverter.setRedisTemplate(redisTemplate);
        return jwtRedisLoginUserConverter;
    }

    /**
     * Jwt token转换器
     */
    @ConditionalOnMissingBean(name = "redisTemplate")
    @Bean("jwtConverter")
    public Converter<Jwt, AbstractAuthenticationToken> defaultJwtConverter(){
        return new JwtBearerTokenAuthenticationConverter();
    }

    @Bean
    public RSAPublicKey rsaPublicKey() {
        return RsaKeyConverters.x509().convert(new ByteArrayInputStream(capyjaraWebStarterProperty.getJwtDecoderPublicKey().getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    /**
     * 支持从Request Header、Cookie中解析BearerToken
     * Spring Security默认使用{@link DefaultBearerTokenResolver}
     *
     * @see DefaultBearerTokenResolver
     * @see CookieBearerTokenResolver
     */
    @Bean
    public BearerTokenResolver bearerTokenResolver(){
        List<BearerTokenResolver> resolverList = List.of(
                new DefaultBearerTokenResolver(),
                new CookieBearerTokenResolver()
        );

        return new DelegateBearerTokenResolver(resolverList);
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new UnauthorizedEntryPoint();
    }

    @ConditionalOnMissingBean
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new ForbiddenEntryPoint();
    }



}
