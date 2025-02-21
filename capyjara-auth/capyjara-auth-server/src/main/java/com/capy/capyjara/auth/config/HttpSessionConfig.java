package com.capy.capyjara.auth.config;

import com.capy.capyjara.auth.jackson.LoginUserMixin;
import com.capy.capyjara.auth.jackson.LongMixin;
import com.capy.capyjara.common.security.LoginUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.jackson2.WebServletJackson2Module;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;

//@EnableRedisHttpSession(redisNamespace = "spring:session:auth")
@EnableRedisIndexedHttpSession(redisNamespace = "spring:session:auth")
@Configuration
public class HttpSessionConfig {


    /**
     * 替换Spring Redis Session默认的序列化方式
     *
     * @see <a href="https://github.com/spring-projects/spring-session/issues/568">spring-session issues 568</a>
     * @see <a href=https://github.com/spring-projects/spring-session/issues/2227>spring-session issues 2227</a>
     * @see <a href=https://github.com/spring-projects/spring-session/issues/2305>spring-session issues 2305</a>
     *
     * @return redisSerializer
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new CoreJackson2Module());
        objectMapper.registerModule(new WebServletJackson2Module());
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

        objectMapper.addMixIn(LoginUser.class, LoginUserMixin.class);

        //  解决Long类型 Spring Session 与 Spring Security序列化方式冲突
        //  https://github.com/spring-projects/spring-session/issues/2227
        //  https://github.com/spring-projects/spring-session/issues/2305
        objectMapper.addMixIn(Long.class, LongMixin.class);
        return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

    }
}
