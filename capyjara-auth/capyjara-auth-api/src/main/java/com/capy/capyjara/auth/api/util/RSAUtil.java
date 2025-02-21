package com.capy.capyjara.auth.api.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;

@Slf4j
public class RSAUtil {

    public static String privateKeyDecode(String base64Str, RSAPrivateKey privateKey) {
        byte[] rsaEncodePassword = Base64.decode(base64Str);
        return new String(SecureUtil.rsa(privateKey.getEncoded(), null).decrypt(rsaEncodePassword, KeyType.PrivateKey), StandardCharsets.UTF_8);
    }
}
