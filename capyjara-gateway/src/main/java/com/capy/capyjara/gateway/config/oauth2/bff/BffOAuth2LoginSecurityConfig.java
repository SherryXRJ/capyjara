package com.capy.capyjara.gateway.config.oauth2.bff;

import com.capy.capyjara.common.constant.Constant;
import com.capy.capyjara.gateway.config.GatewayServerProperties;
import com.capy.capyjara.gateway.config.oauth2.MultiDeviceServerOAuth2AuthorizationRequestResolver;
import com.capy.capyjara.gateway.config.oauth2.ReactiveRedisOAuth2AuthorizedClientService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.session.ReactiveSessionRegistry;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.SessionLimit;
import org.springframework.session.data.redis.ReactiveRedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisIndexedWebSession;
import org.springframework.session.security.SpringSessionBackedReactiveSessionRegistry;

import java.time.Duration;
import java.util.Objects;

/**
 * BFF模式下的Spring Security oauth2login配置
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@Import({RedisSessionProperties.class})
@ConditionalOnProperty(prefix = Constant.CONFIG_PREFIX + "gateway", name = "frontendBackendMode", havingValue = "bff", matchIfMissing = true)
public class BffOAuth2LoginSecurityConfig implements InitializingBean {

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    private final ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    private final ReactiveRedisIndexedSessionRepository reactiveRedisIndexedSessionRepository;

    private final GatewayServerProperties gatewayServerProperties;

    private final SessionProperties sessionProperties;

    private final RedisSessionProperties redisSessionProperties;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .sessionManagement(sessionManagementSpec -> sessionManagementSpec.concurrentSessions(concurrentSessionsSpec ->
                        concurrentSessionsSpec
                                //  单用户最大会话数
                                .maximumSessions(SessionLimit.of(gatewayServerProperties.getMaximumSessions()))
                                //  session管理
                                .sessionRegistry(reactiveSessionRegistry())))
                .oauth2Login(oAuth2LoginSpec -> oAuth2LoginSpec
                        //  oauth2login前置处理
                        .authorizationRequestResolver(serverOAuth2AuthorizationRequestResolver())
                                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler(gatewayServerProperties.getOAuth2LoginSuccessRedirectUri()))
                        //  todo: 失败处理
//                        .authenticationFailureHandler()
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    /**
     * 替换通过内存存储的 {@link InMemoryReactiveOAuth2AuthorizedClientService}。
     * 使用自定义实现的{@link ReactiveRedisOAuth2AuthorizedClientService}通过redis存储已授权oauth信息。
     */
    @Bean
    public ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService(){
        ReactiveRedisOAuth2AuthorizedClientService oAuth2AuthorizedClientService = new ReactiveRedisOAuth2AuthorizedClientService(reactiveRedisTemplate, reactiveClientRegistrationRepository);
        oAuth2AuthorizedClientService.setOAuth2AuthorizedLiveDuration(sessionProperties.getTimeout());
        return oAuth2AuthorizedClientService;
    }

    /**
     * 分布式session管理
     * <p>
     * <b>Warn:</b>在服务宕机，或者Redis Cluster模式下，Spring无法监听到Session的过期事件，从而不会从Redis中{@code index:PRINCIPAL_NAME_INDEX_NAME} 索引中移除对应的SessionId。
     * 残留在Redis索引中的SessionId可能会额外增加Redis内存开销。
     *
     * @see SpringSessionBackedReactiveSessionRegistry
     * @see org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession
     * @see <a href="https://github.com/spring-projects/spring-session/issues/2906">spring-session issues 2906</a>
     * @see <a href="https://docs.spring.io/spring-session/reference/3.3/configuration/reactive-redis-indexed.html#how-spring-session-cleans-up-expired-sessions">Spring Session 监听Session过期事件及处理策略(How Spring Session Cleans Up Expired Sessions)</a>
     */
    @Bean
    public ReactiveSessionRegistry reactiveSessionRegistry(){
        //  Fixme: 增加额外的定时任务来处理那些已经过期但没有从Redis索引中移除的会话信息？
        // @see https://docs.spring.io/spring-session/reference/3.3/configuration/reactive-redis-indexed.html#taking-control-over-the-cleanup-task
        return new SpringSessionBackedReactiveSessionRegistry<>(reactiveRedisIndexedSessionRepository, reactiveRedisIndexedSessionRepository);
    }

    /**
     * 通过{@link ServerOAuth2AuthorizationRequestResolver}实现OAuth2Login前置处理流程。
     *
     * @see MultiDeviceServerOAuth2AuthorizationRequestResolver
     */
    @Bean
    public ServerOAuth2AuthorizationRequestResolver serverOAuth2AuthorizationRequestResolver(){
        MultiDeviceServerOAuth2AuthorizationRequestResolver auth2AuthorizationRequestResolver = new MultiDeviceServerOAuth2AuthorizationRequestResolver(reactiveClientRegistrationRepository);
        auth2AuthorizationRequestResolver.setMaxCookieAge(sessionProperties.getTimeout());
        return auth2AuthorizationRequestResolver;
    }

    /**
     * Bean初始化后操作
     * <p>
     * Spring似乎无法通过application.yaml配置文件修改Spring Session的namespace和MaxInactiveInterval配置。
     * 手动进行处理
     */
    @Override
    public void afterPropertiesSet() {
        EnableRedisIndexedWebSession indexedWebSession = AnnotationUtils.findAnnotation(WebSessionConfig.class, EnableRedisIndexedWebSession.class);

        String namespace = StringUtils.isBlank(redisSessionProperties.getNamespace()) ?
                indexedWebSession.redisNamespace() : redisSessionProperties.getNamespace();

        Duration maxInactiveInterval = Objects.isNull(sessionProperties.getTimeout()) ?
                Duration.ofSeconds(indexedWebSession.maxInactiveIntervalInSeconds()) : sessionProperties.getTimeout();

        reactiveRedisIndexedSessionRepository.setRedisKeyNamespace(namespace);
        reactiveRedisIndexedSessionRepository.setDefaultMaxInactiveInterval(maxInactiveInterval);
    }
}
