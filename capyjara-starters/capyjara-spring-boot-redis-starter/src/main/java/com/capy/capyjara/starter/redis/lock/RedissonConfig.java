//package com.capy.lightweight.redis.lock;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.redisson.spring.starter.RedissonProperties;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//
//@Configuration
//@ConditionalOnProperty
//public class RedissonConfig {
//
//    @Bean
//    public RedissonClient redissonClient(RedissonProperties redissonProperties) throws IOException {
//        Config config = Config.fromYAML(redissonConfig);
//        return Redisson.create(config);
//    }
//}
