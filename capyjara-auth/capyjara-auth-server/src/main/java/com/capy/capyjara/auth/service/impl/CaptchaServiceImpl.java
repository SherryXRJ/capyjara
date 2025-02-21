package com.capy.capyjara.auth.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.capy.capyjara.auth.entity.vo.CaptchaVO;
import com.capy.capyjara.auth.service.CaptchaService;
import com.capy.capyjara.starter.redis.cache.RedisCacheClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class CaptchaServiceImpl implements CaptchaService {

    /**
     * 验证码redis-key前缀
     */
    private final static String CAPTCHA_PREFIX = "CAPTCHA:";

    /**
     * 验证码有效期(单位: 秒)
     */
    private final static int EXPIRE = 60;

    @Resource
    private RedisCacheClient redisCacheClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public CaptchaVO generateCaptcha() {
        //  生成4位验证码
        String captchaId = UUID.randomUUID().toString();
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(150, 46, 4, 10);

        String captchaCode = lineCaptcha.getCode();
        String imageBase64 = lineCaptcha.getImageBase64Data();

        //  将验证码存储到redis
        String cacheKey = CAPTCHA_PREFIX + captchaId;
        redisCacheClient.setValueTimeout(cacheKey, captchaCode, EXPIRE);

        log.info("=> 生成验证码:captchaId{}, captchaCode:{}", captchaId, captchaCode);

        return CaptchaVO.builder()
                .captchaId(captchaId)
                .captcha(imageBase64)
                .build();
    }

    @Override
    public boolean verifyCaptchaCode(String captchaId, String code) {
        String cacheKey = CAPTCHA_PREFIX + captchaId;
        String realCode = (String) redisCacheClient.getValue(cacheKey);

        boolean isSuccess = code.equals(realCode);
        if (isSuccess) {
            redisCacheClient.delKey(cacheKey);
        }
        return isSuccess;
    }

    @Override
    public boolean isCaptchaExpired(String captchaId) {
        String cacheKey = CAPTCHA_PREFIX + captchaId;
        return Objects.isNull(redisCacheClient.getValue(cacheKey));
    }


}
