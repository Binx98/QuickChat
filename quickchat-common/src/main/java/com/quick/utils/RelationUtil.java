package com.quick.utils;

/**
 * @Author 徐志斌
 * @Date: 2023/12/30 19:05
 * @Version 1.0
 * @Description:
 */
public class RelationUtil {
    /**
     * 通过参数生成：关系字符串
     */
    public static String generate(String fromId, String toId) {
        if (toId.compareTo(fromId) <= 0) {
            return toId + ":" + fromId;
        } else {
            return fromId + ":" + toId;
        }
    }
}
