package com.capy.capyjara.starter.redis.cache;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

/**
 * 支持配置过期时间的RedisCacheManager <p>
 * 过期时间单位: 分钟
 *
 * <pre>
 * {@code
 *
 *      设置500分钟的缓存
 *      @Cacheable(value = "cacheName:500")
 *      public String get(){
 *          return "data";
 *      }
 * }
 * </pre>
 */
@Deprecated
public class TtlRedisCacheManager extends RedisCacheManager {

    /**
     * 默认过期时间(分钟)
     */
    private final static long CACHE_DEFAULT_EXPIRE_TIME = 1;

    public TtlRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        String[] split = name.split(":");

        String cacheName = split[0];
        long ttl = split.length > 1 ? Long.parseLong(split[1]) : CACHE_DEFAULT_EXPIRE_TIME;
        RedisCacheConfiguration ttlConfig = cacheConfig.entryTtl(Duration.ofMinutes(ttl));

        return super.createRedisCache(cacheName, ttlConfig);
    }

}
