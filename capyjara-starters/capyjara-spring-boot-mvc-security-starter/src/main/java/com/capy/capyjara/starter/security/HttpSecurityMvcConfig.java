package com.capy.capyjara.starter.security;

import com.capy.capyjara.common.constant.Constant;
import com.capy.capyjara.starter.MvcSecurityStarterProperties;
import com.capy.capyjara.starter.mvc.FeignConfig;
import com.capy.capyjara.starter.security.converter.CookieBearerTokenResolver;
import com.capy.capyjara.starter.security.converter.DelegateBearerTokenResolver;
import com.capy.capyjara.starter.security.converter.DelegateJwtBearerTokenAuthenticationConverter;
import com.capy.capyjara.starter.security.converter.JwtLoginUserConverter;
import com.capy.capyjara.starter.security.exception.ForbiddenEntryPoint;
import com.capy.capyjara.starter.security.exception.UnauthorizedEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
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
@EnableConfigurationProperties(MvcSecurityStarterProperties.class)
@RequiredArgsConstructor
public class HttpSecurityMvcConfig {

    private final MvcSecurityStarterProperties mvcSecurityStarterProperties;

    /**
     * OAuth2 Resource Server 过滤器链
     * <p>
     * 默认资源服务器过滤链配置, 将业务API交由Spring Security进行管理
     * <p>
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
                    //  fixme : 去掉 通过注解 Or oauth2 client credential?
//                    .requestMatchers("/internal/**").access(new InternalRequestAuthorizationManager(mvcSecurityStarterProperties.getInternalRequestSecretKey()))
                    .requestMatchers(mvcSecurityStarterProperties.getWhitelistUrl()).permitAll()
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
     * OAuth2 Client配置
     * <p>
     * 各个业务微服务可以作为OAuth2 Resource Server被调用(需要携带已授权用户的access_token)。
     * 但在某些场景下，微服务之间的RPC调用无法获取用户的access_token
     * (例如服务A中存在一个定时任务，该定时任务需要通过RPC调用服务B的接口；该场景下服务A无法持有授权用户的access_token)
     * <p>
     * 为了解决这种场景下的接口鉴权问题，可以将各个微服务当做OAuth2 Client，即主动向auth-server(及其他支持OAuth2授权的IdP)发起认证授权，获取access_token
     * <p>
     * <b>Note:</b> 默认只开启了client_credentials模式
     *
     * @param clientRegistrationRepository 通过application.yaml文件配置 采用{@link InMemoryClientRegistrationRepository}
     * @param authorizedClientRepository   采用Spring Security自动管理注入的{@link OAuth2AuthorizedClientRepository}
     */
    @ConditionalOnProperty(prefix = Constant.CONFIG_PREFIX + "mvc-security", name = "enableOAuth2Client", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(OAuth2AuthorizedClientManager.class)
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        //  只启用client_credentials模式
                        .clientCredentials()
                        .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @ConditionalOnBean(name = "redisTemplate")
    @Bean("jwtConverter")
    public Converter<Jwt, AbstractAuthenticationToken> redisCacheJwtConverter(RedisTemplate<String, Object> redisTemplate){
        JwtLoginUserConverter jwtLoginUserConverter = new JwtLoginUserConverter();
        jwtLoginUserConverter.setRedisTemplate(redisTemplate);

        //  converter存在顺序优先级
        //  1.LoginUser 2.OAuth2AuthenticatedPrincipal 3.Jwt
        return new DelegateJwtBearerTokenAuthenticationConverter(
                List.of(jwtLoginUserConverter,
                        new JwtBearerTokenAuthenticationConverter(),
                        new JwtAuthenticationConverter()
                )
        );
    }

    @ConditionalOnMissingBean(name = "redisTemplate")
    @Bean("jwtConverter")
    public Converter<Jwt, AbstractAuthenticationToken> defaultJwtConverter(){
        return new JwtBearerTokenAuthenticationConverter();
    }

    @Bean
    public RSAPublicKey rsaPublicKey() {
        return RsaKeyConverters.x509().convert(new ByteArrayInputStream(mvcSecurityStarterProperties.getJwtDecoderPublicKey().getBytes()));
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
