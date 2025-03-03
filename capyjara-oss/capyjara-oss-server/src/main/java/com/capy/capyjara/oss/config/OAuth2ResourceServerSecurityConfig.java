package com.capy.capyjara.oss.config;


import com.capy.capyjara.starter.security.converter.JwtLoginUserConverter;
import com.capy.capyjara.swagger.SwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtLoginUserConverter jwtLoginUserConverter) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(SwaggerConfig.SWAGGER_URLS).permitAll()
                                .anyRequest().authenticated()
                )
                .cors(Customizer.withDefaults())
                .sessionManagement(policy -> policy.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer
                        -> jwtConfigurer.jwtAuthenticationConverter(jwtLoginUserConverter)))
//                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
//                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
//                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint());
//                })
        ;
        return http.build();
    }

}
