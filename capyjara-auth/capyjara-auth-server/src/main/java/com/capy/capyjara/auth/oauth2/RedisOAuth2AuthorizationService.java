package com.capy.capyjara.auth.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

/**
 *
 * @see <a href="https://docs.spring.io/spring-authorization-server/reference/guides/how-to-redis.html">Spring Security How-to: Implement core services with Redis</a>
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/tree/main/docs/src/main/java/sample/redis">Redis Sample</a>
 */
@Slf4j
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {


    private String namespace = "auth:oauth2:authorization";

    private RedisTemplate<String, Object> redisTemplate;


    public RedisOAuth2AuthorizationService(String namespace, RedisTemplate<String, Object> redisTemplate) {
        this.namespace = namespace;
        this.redisTemplate = redisTemplate;
    }

    public RedisOAuth2AuthorizationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        String authorizationId = authorization.getId();

    }

    @Override
    public void remove(OAuth2Authorization authorization) {

    }

    @Override
    public OAuth2Authorization findById(String id) {
        return null;
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        return null;
    }
}
