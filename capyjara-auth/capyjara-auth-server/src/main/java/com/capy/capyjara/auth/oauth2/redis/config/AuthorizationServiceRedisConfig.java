package com.capy.capyjara.auth.oauth2.redis.config;

import com.capy.capyjara.auth.oauth2.redis.convert.*;
import com.capy.capyjara.auth.oauth2.redis.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import com.capy.capyjara.auth.oauth2.redis.repository.OAuth2UserConsentRepository;
import com.capy.capyjara.auth.oauth2.redis.service.RedisOAuth2AuthorizationConsentService;
import com.capy.capyjara.auth.oauth2.redis.service.RedisOAuth2AuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Arrays;

/**
 * Authorization Service Redis配置
 * @see <a href="https://docs.spring.io/spring-authorization-server/reference/guides/how-to-redis.html#configure-core-services">how to redis</a>
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/docs/src/main/java/sample/redis/config/RedisConfig.java">github sample</a>
 */
@EnableRedisRepositories(
        basePackages = "com.capy.capyjara.auth.oauth2.redis.repository"
)
@Configuration
public class AuthorizationServiceRedisConfig {


    @Bean
    public RedisCustomConversions redisCustomConversions() {
        return new RedisCustomConversions(Arrays.asList(
                new UsernamePasswordAuthenticationTokenToBytesConverter(),
                new BytesToUsernamePasswordAuthenticationTokenConverter(),
                new OAuth2AuthorizationRequestToBytesConverter(), new BytesToOAuth2AuthorizationRequestConverter(),
                new ClaimsHolderToBytesConverter(), new BytesToClaimsHolderConverter()));
    }

    @Bean
    public RedisOAuth2AuthorizationService authorizationService(RegisteredClientRepository registeredClientRepository,
                                                                OAuth2AuthorizationGrantAuthorizationRepository authorizationGrantAuthorizationRepository,
                                                                RedisTemplate<String, Object> redisTemplate) {
        return new RedisOAuth2AuthorizationService(registeredClientRepository,
                authorizationGrantAuthorizationRepository,
                redisTemplate);
    }

    @Bean
    public RedisOAuth2AuthorizationConsentService authorizationConsentService(
            OAuth2UserConsentRepository userConsentRepository) {
        //  todo: 调试
        return new RedisOAuth2AuthorizationConsentService(userConsentRepository);
    }
}
