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

    public static void setData(Map<String, Object> map) {
        threadLocal.set(map);
    }

    public static Map<String, Object> getData() {
        return threadLocal.get();
    }

    public static void removeData() {
        threadLocal.remove();
    }
}
