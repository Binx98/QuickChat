package com.quick.enums;

/**
 * @Author 徐志斌
 * @Date: 2024/2/14 20:48
 * @Version 1.0
 * @Description: 限流类型枚举
 */
public enum LimitTypeEnum {
    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP
}
