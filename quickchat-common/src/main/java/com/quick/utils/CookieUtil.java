package com.quick.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author 徐志斌
 * @Date: 2023/8/19 16:27
 * @Version 1.0
 * @Description: Cookie工具类
 */
public class CookieUtil {
    /**
     * 从 Cookie 中取出某个 key 的值
     */
    public static String getValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        String v = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(key)) {
                v = cookie.getValue();
                break;
            }
        }
        return v;
    }

    /**
     * 更新已存在 Cookie 数据
     */
    public static void setValue(HttpServletRequest request, HttpServletResponse response, String key, String value) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(key)) {
                cookie.setValue(value);
                response.addCookie(cookie);
                break;
            }
        }
    }

    /**
     * 判断 Cookie 中是否含有某个 key
     */
    public static boolean hasCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return false;
        boolean has = false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(key)) {
                has = true;
                break;
            }
        }
        return has;
    }

    /**
     * 新增 Cookie 信息
     */
    public static void addCookie(HttpServletResponse response, String key, String value, boolean secure, int expiry, String path) {
        Cookie cookie = new Cookie(key, value);
        cookie.setValue(value);
        cookie.setSecure(secure);
        cookie.setMaxAge(expiry);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    /**
     * 移除 Cookie 中的某个 Key
     */
    public static void clearCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(key)) {
                cookie.setMaxAge(0);
                cookie.setValue(null);
                response.addCookie(cookie);
            }
        }
    }

    /**
     * 清除所有 Cookie
     */
    public static void clearAll(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            response.addCookie(cookie);
        }
    }
}
