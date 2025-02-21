package com.capy.capyjara.auth.service;

import com.capy.capyjara.auth.entity.vo.CaptchaVO;

public interface CaptchaService {

    /**
     * 生成验证码
     *
     * @return 验证码信息
     */
    CaptchaVO generateCaptcha();

    /**
     * 校验验证码是否正确
     *
     * @return true:验证成功, false:验证失败
     */
    boolean verifyCaptchaCode(String captchaId, String code);

    /**
     * 校验验证码是否过期
     * @param captchaId 验证码id
     * @return true:过期, false:未过期
     */
    boolean isCaptchaExpired(String captchaId);
}
