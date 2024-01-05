package com.quick.utils;

import java.util.Map;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-08-29  14:53
 * @Description: 请求全局上下文
 * @Version: 1.0
 */
public class RequestContextUtil {
    public static final String ACCOUNT_ID = "account_id";
    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    /**
     * 保存设置信息
     */
    public static void set(Map<String, Object> map) {
        threadLocal.set(map);
    }

    /**
     * 获取信息
     */
    public static Map<String, Object> get() {
        return threadLocal.get();
    }

    /**
     * 移除信息
     */
    public static void remove() {
        threadLocal.remove();
    }
}
