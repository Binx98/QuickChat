package com.quick.aspect;

import com.quick.annotation.RateLimiter;
import com.quick.config.LuaScriptConfig;
import com.quick.enums.LimitTypeEnum;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.utils.HttpServletUtil;
import com.quick.utils.IpUtil;
import com.quick.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

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
    private RedisUtil redisUtil;
    @Autowired
    @Qualifier(LuaScriptConfig.RATE_LIMIT_LUA)
    private RedisScript<Long> limitScript;

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        String key = rateLimiter.key();
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String combineKey = getCombineKey(rateLimiter, point);
        List<Object> keys = Collections.singletonList(combineKey);
        Long number = redisUtil.executeScript(limitScript, keys, count, time);
        if (number == null || number.intValue() > count) {
            throw new QuickException(ResponseEnum.SEND_MSG_FAST);
        }
        log.info("--------限制请求'{}',当前请求'{}',缓存key'{}'--------", count, number.intValue(), key);
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuffer buffer = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitTypeEnum.IP) {
            buffer.append(IpUtil.getIpAddr(HttpServletUtil.getRequest())).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        buffer.append(targetClass.getName()).append("-").append(method.getName());
        return buffer.toString();
    }
}
