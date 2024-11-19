package com.quick.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-03-24  17:46
 * @Description: AES加密工具类
 * @Version: 1.0
 */
@Slf4j
public class AESUtil {
    // 加密 Key 长度确保 16 位
    private static final String KEY = "QuickChat_XZB!!!";

    public static String encrypt(String content) throws Exception {
        byte[] raw = KEY.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return new Base64().encodeAsString(encrypted);
    }

    public static String decrypt(String content) throws Exception {
        byte[] raw = KEY.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encrypted1 = new Base64().decode(content);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        String encryptPwd = encrypt("xuzhibin");
        System.out.println(encryptPwd);
    }
}
