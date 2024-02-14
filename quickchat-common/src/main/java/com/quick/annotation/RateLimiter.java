package com.quick.annotation;

import com.quick.enums.LimitType;

import java.lang.annotation.*;

/**
 * @Author 徐志斌
 * @Date: 2024/2/14 20:40
 * @Version 1.0
 * @Description: 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     */
    String key() default "rate_limit:";

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;
}

