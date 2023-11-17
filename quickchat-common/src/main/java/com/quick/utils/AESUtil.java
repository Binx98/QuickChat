package com.quick.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-03-24  17:46
 * @Description: AES加密工具类
 * @Version: 1.0
 */
@Slf4j
public class AESUtil {
    // AES加密Key为16位
    private static final String KEY = "PXY_PARAVERSE_DP";

    /**
     * 加密
     */
    public static String encrypt(String content) throws Exception {
        byte[] raw = KEY.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // "算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
        return new Base64().encodeToString(encrypted); // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /**
     * 解密
     */
    public static String decrypt(String content) throws Exception {
        byte[] raw = KEY.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = new Base64().decode(content);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "utf-8");
        return originalString;
    }

    public static void main(String[] args) throws Exception {
        String encryptPwd = encrypt("admin123");
        System.out.println(encryptPwd);
    }
}
