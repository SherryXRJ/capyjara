package com.capy.capyjara.auth.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;
import com.capy.capyjara.auth.api.constant.AuthResultEnum;
import com.capy.capyjara.auth.oauth2.OAuth2JwtTokenCustomizer;
import com.capy.capyjara.auth.oauth2.redis.service.RedisOAuth2AuthorizationService;
import com.capy.capyjara.auth.service.impl.BEndUserDetailServiceImpl;
import com.capy.capyjara.common.response.Result;
import com.capy.capyjara.starter.MvcSecurityStarterProperties;
import com.capy.capyjara.starter.security.exception.ForbiddenEntryPoint;
import com.capy.capyjara.starter.security.exception.UnauthorizedEntryPoint;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.time.Duration;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AuthSecurityConfig {

    private final AccessDeniedHandler accessDeniedHandler = new ForbiddenEntryPoint();

    private final AuthenticationEntryPoint authenticationEntryPoint = new UnauthorizedEntryPoint();

    private final JdbcTemplate jdbcTemplate;

    private final MvcSecurityStarterProperties mvcSecurityStarterProperties;

    private final AuthServerProperties authServerProperties;

    private final RedisTemplate<String, Object> redisTemplate;

    private final SessionProperties sessionProperties;


    /* ------------ Spring Security OAuth2 AuthorizationServer Configuration --------------*/

    /**
     *
     * 基于Spring Security OAuth2默认配置进行修改调整 {@link OAuth2AuthorizationServerConfigurer}
     * <p>
     * OAuth2相关配置(该配置只对 "/oauth2/**" 相关接口生效)
     * <p>
     * 需要注意OAuth2只进行授权(Authorization)，并不直接进行认证(Authentication)
     * 为了便于区分这2个流程, 该配置类中分别配置了认证、授权 2条SecurityFilterChain
     *
     * @see OAuth2AuthorizationServerConfigurer
     * @see OAuth2JwtTokenCustomizer
     * @see com.capy.capyjara.auth.oauth2.redis.config.AuthorizationServiceRedisConfig
     * @see RedisOAuth2AuthorizationService
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain oAuth2authorizationServerSecurityFilterChain(HttpSecurity http, RedisOAuth2AuthorizationService redisOAuth2AuthorizationService) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        //  可自定义修改OAuth2相关配置
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = http.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

        authorizationServerConfigurer
                .authorizationService(redisOAuth2AuthorizationService)
                .oidc(withDefaults())   //  启用Oidc
        ;

        return http
                .formLogin(withDefaults())
                .build();
    }

    /**
     * Authorization Server作为认证服务对接多个Client
     * (例如 其他第三方Web系统可对接至该认证服务, 实现系统间的单点跳转) <br/>
     *
     * Client Repository 就是持久化存储Client信息(client_id client_secret等)的媒介<br/>
     *
     * Spring Security内置了JDBC Repository {@link JdbcRegisteredClientRepository}
     *
     * @see <a href="https://docs.spring.io/spring-authorization-server/reference/1.2/guides/how-to-pkce.html">Spring Security how-to-pkce<a/>
     */
    @Bean
    public RegisteredClientRepository clientRepository(){
//        return new JdbcRegisteredClientRepository(jdbcTemplate);
        RegisteredClient client = RegisteredClient
                .withId("capyId")
                .clientId("capyId")
                .clientSecret("{noop}capySecret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                //  支持PKCE
                .clientAuthenticationMethod(new ClientAuthenticationMethod("sha256"))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofSeconds(100)).build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).requireProofKey(false).build())
                .redirectUri("http://192.168.216.138:8080/login/oauth2/code/capyId")
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.OPENID)
                .scope("myscope")
                .build();
        InMemoryRegisteredClientRepository repository = new InMemoryRegisteredClientRepository(client);
        return repository;
    }

    /**
     * 可自定义授权uri地址
     *
     * @see <a href="https://docs.spring.io/spring-authorization-server/reference/configuration-model.html#configuring-authorization-server-settings">Spring Doc: AuthorizationServerSettings 配置</a>
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings
                //  使用Spring Security默认OAuth2、Oidc各个接口的地址
                .builder()
                .build();
    }

    /* ----------- Spring Security OAuth2 AuthorizationServer Configuration end ---------------*/


    /**
     *
     * 认证(Authentication)FilterChain,
     * 该FilterChain的优先级低于{@link AuthSecurityConfig#oAuth2authorizationServerSecurityFilterChain(HttpSecurity, RedisOAuth2AuthorizationService)}
     *
     * 提供各类认证方式(账密、短信、邮件)
     *
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public SecurityFilterChain authenticationFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/login/**"
//                        , "/user"
                )
                .authorizeHttpRequests(authorize ->
                    authorize.requestMatchers("/login/**").permitAll()
                            .anyRequest().authenticated()
                )
                //  验证码校验优先级 高于 账密验证
//                .addFilterBefore(captchaAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(userTypeFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests((authz) ->
//                        authz.requestMatchers(capyjaraWebStarterProperty.getWhitelistUrl()).permitAll()
//                )
                //  登录功能配置
//                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
//                    authorizationManagerRequestMatcherRegistry.requestMatchers("/login", "/captcha/**").permitAll()
//                    .requestMatchers("/test/**").authenticated()
//                    ;
//                })
                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer
                                .failureHandler(failureHandler())
                )
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(new JwtAuthenticationConverter()))
                )
                .userDetailsService(new BEndUserDetailServiceImpl())
                //  认证方式 1.B端账密 2.短信验证码
                .authenticationProvider(new BEndUsernamePasswordAuthenticationProvider(passwordEncoder(), new BEndUserDetailServiceImpl()))
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(policy -> {
                    policy.maximumSessions(authServerProperties.getMaximumSessions());
                    policy.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                })
//                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
//                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler);
//                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
//                })
                //  todo:修改注销策略
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer.logoutSuccessHandler(logoutSuccessHandler()))
        ;

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return CustomizedPasswordEncoderFactory.createPasswordEncoder(authServerProperties.getBcryptLength());
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) ->
                writeJsonResponse(response, Result.fail(AuthResultEnum.LOGIN_FAIL.getCode(), exception.getMessage()));
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> writeJsonResponse(response, Result.ok("登出成功"));
    }



//    @Bean
//    public AuthorizationServerSettings providerSettings() {
//        return AuthorizationServerSettings.builder().build();
//    }


//    @Bean
//    public UserDetailsChecker userDetailsChecker() {
//        return new AdminUsernamePasswordAuthenticationProvider.AuthenticationChecker(accountLockTime);
//    }

    public static void writeJsonResponse(HttpServletResponse response, Result result) throws IOException {
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String json = jsonMapper.writeValueAsString(result);
        response.setContentType(ContentType.JSON.getValue());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
