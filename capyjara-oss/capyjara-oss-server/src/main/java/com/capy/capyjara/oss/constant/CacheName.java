package com.capy.capyjara.oss.constant;

import com.capy.capyjara.starter.redis.cache.TtlRedisCacheManager;

/**
 * 缓存key
 * 数字代表缓存的时间(分钟)
 *
 * @see TtlRedisCacheManager
 */
public interface CacheName {

    String GET_PRESIGNED_URL = "getPreSignedURL:3";

    String STAT_OBJECT = "statObject:5";
}
