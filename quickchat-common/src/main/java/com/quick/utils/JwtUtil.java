package com.quick.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:46
 * @Version 1.0
 * @Description: JWT工具类
 */
public class JwtUtil {
    public static final long EXPIRE = 86400000;
    public static final String JWT_SECRET = "Quick_Chat_Xu_1998";

    /**
     * 根据 uid 生成 Token
     */
    public static String generate(String accountId) {
        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("QuickChat")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim(RequestContextUtil.ACCOUNT_ID, accountId)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
        return JwtToken;
    }

    /**
     * 解析 Token
     */
    public static Map<String, Object> resolve(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String accountId = (String) claims.get(RequestContextUtil.ACCOUNT_ID);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(RequestContextUtil.ACCOUNT_ID, accountId);
        return resultMap;
    }

    /**
     * 校验 Token 是否可用
     */
    public static boolean check(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
    }
}
