package com.capy.capyjara.gateway.config.oauth2.bff;

import com.capy.capyjara.common.constant.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisIndexedWebSession;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.session.security.SpringSessionBackedReactiveSessionRegistry;

import java.util.Optional;

/**
 * WebSession配置
 * 需注意{@link EnableRedisWebSession} 与 {@link EnableRedisIndexedWebSession} 之间的区别
 *
 * 在bff架构中集成了{@link SpringSessionBackedReactiveSessionRegistry} 因此使用了 {@link EnableRedisIndexedWebSession}
 *
 * 通过application.yaml进行配置
 * <pre>
 * {@code
 * @EnableRedisIndexedWebSession(redisNamespace = "spring:session:gateway", maxInactiveIntervalInSeconds = 3600)
 * }
 * 等同于以下配置
 * </pre>
 * <pre>
 * {@code
 * spring:
 *   session:
 *     redis:
 *       repository-type: indexed
 *       namespace: "spring:session:gateway"
 *     timeout: 3600
 * }
 * </pre>
 *
 * @see EnableRedisIndexedWebSession
 * @see EnableRedisWebSession
 * @see BffOAuth2LoginSecurityConfig
 * @see SpringSessionBackedReactiveSessionRegistry
 */
@Configuration
@EnableRedisIndexedWebSession
@ConditionalOnProperty(prefix = Constant.CONFIG_PREFIX + "gateway", name = "frontendBackendMode", havingValue = "bff", matchIfMissing = true)
public class WebSessionConfig {



    /**
     * 替换Spring Redis Session默认的序列化方式
     * @see <a href="https://github.com/spring-projects/spring-session/issues/568">spring-session issues 568</a>
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
        return new Jackson2JsonRedisSerializer<>(objectMapper(), Object.class);
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new CoreJackson2Module());
        objectMapper.registerModule(new JavaTimeModule());
        //  集成OAuth2 Login
        objectMapper.registerModule(new OAuth2ClientJackson2Module());
        return objectMapper;
    }
}
