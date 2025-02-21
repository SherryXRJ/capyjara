package com.capy.capyjara.admin.auth.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.ContentType;
import com.capy.capyjara.admin.auth.config.convert.JwtLoginAdminUserConverter;
import com.capy.capyjara.swagger.SwaggerConfig;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.capy.capyjara.common.response.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.crypto.RsaKeyConversionServicePostProcessor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
//@EnableMethodSecurity
//@EnableWebSecurity
//@Slf4j
//@RefreshScope
public class SecurityAuthConfig {

    /**
     * Spring Security 内置登录流程接口的Endpoint
     */
    public final static String SPRING_SECURITY_LOGIN_ENDPOINT = "/auth/security/login";

    /**
     * Spring MVC 自定义登录接口的Endpoint
     */
    public final static String CUSTOM_LOGIN_ENDPOINT = "/auth/login";


    @Value("${account.lock.time:15}")
    private Integer accountLockTime;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtLoginAdminUserConverter jwtLoginAdminUserConverter,
                                                   AuthenticationEntryPoint authenticationEntryPoint,
                                                   AccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                // 配置需要放行、权限拦截的接口地址
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SwaggerConfig.SWAGGER_URLS).permitAll()
                        .requestMatchers(CUSTOM_LOGIN_ENDPOINT, "/auth/captcha/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(httpSecurityFormLoginConfigurer ->
                    //  修改Spring Security登录接口的Endpoint请求地址
                    //  登录请求该Endpoint时，不需要在MVC Controller中定义接口，默认直接走Spring Security内置的Filter流程
                    //  如果想自己定义一个Controller接口，也可以通过authenticationConfiguration.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password)) 方式触发登录流程
                    httpSecurityFormLoginConfigurer
                            .loginProcessingUrl(SPRING_SECURITY_LOGIN_ENDPOINT).permitAll()
                         // .loginPage()    // 其余配置按需增加
                )
//                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
//                        httpSecurityOAuth2ResourceServerConfigurer
//                                //  JWT token解析转为登录对应实体
//                                .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtLoginAdminUserConverter))
//                                //  未认证访问异常处理   (例如:未登录)
//                                .authenticationEntryPoint(authenticationEntryPoint())
//                                //  未授权访问异常处理   (例如:越权访问)
//                                .accessDeniedHandler(accessDeniedHandler())
//
//                )
                //  因为使用JWT方案，因此禁用HTTP Session
//                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //  禁用csrf
                .csrf(AbstractHttpConfigurer::disable)
                //  异常处理
                .exceptionHandling((exceptions) -> exceptions
                        //  未认证访问异常处理   (例如:未登录)
                        .authenticationEntryPoint(authenticationEntryPoint)
                        //  未授权访问异常处理   (例如:越权访问)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .cors(withDefaults())
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //  Fixme: 默认使用BCryptPasswordEncoder 但在并发场景下存在登录TPS较低的问题
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


//    @SuppressWarnings("rawtypes")
//    @Bean
//    public AuthenticationFailureHandler failureHandler() {
//        return (request, response, exception) -> {
//            Result result = Result.fail(exception.getMessage());
//            this.writeJsonResponse(response, result);
//        };
//    }

//    @SuppressWarnings("rawtypes")
//    @Bean
//    public AuthenticationEntryPoint httpAuthenticationEntryPoint() {
//        return (request, response, authException) -> {
//            Result result = Result.fail(authException.getMessage());
//            this.writeJsonResponse(response, result);
//        };
//    }

    @SuppressWarnings("rawtypes")
    private void writeJsonResponse(HttpServletResponse response, Result result) throws IOException {
        JsonMapper jsonMapper = new JsonMapper();
        String json = jsonMapper.writeValueAsString(result);
        response.setContentType(ContentType.JSON.getValue());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * RSA - 用于前、后端参数的加解密
     * Spring Security内置了MVC的公钥、私钥的解析转换 {@link RsaKeyConversionServicePostProcessor}
     * @param privateKey    私钥
     * @param publicKey     公钥
     * @return RSA
     */
    @Bean
    public RSA rsa(@Value("${jwt.private.key}") RSAPrivateKey privateKey, @Value("${jwt.public.key}")RSAPublicKey publicKey){
        return new RSA(privateKey, publicKey);
    }


}
