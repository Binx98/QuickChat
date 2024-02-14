package com.quick.aspect;

import com.quick.annotation.RateLimiter;
import com.quick.enums.LimitType;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.utils.IpUtil;
import com.quick.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2024/2/14 20:41
 * @Version 1.0
 * @Description: 限流切面类
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAspect {
    @Autowired
    private RedisScript<Long> limitScript;
    @Autowired
    private RedisUtil redisUtil;

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        String key = rateLimiter.key();
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String combineKey = getCombineKey(rateLimiter, point);
        List<Object> keys = Collections.singletonList(combineKey);
        Long number = redisUtil.executeScript(limitScript, keys, count, time);
        if (number == null || number.intValue() > count) {
            throw new QuickException(ResponseEnum.FAIL);
        }
        log.info("--------限制请求'{}',当前请求'{}',缓存key'{}'--------", count, number.intValue(), key);
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            stringBuffer.append(IpUtil.getIpAddr(((ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes()).getRequest())).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
