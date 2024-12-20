package com.quick.common.aspect;

import com.quick.common.annotation.RateLimiter;
import com.quick.common.enums.LimitTypeEnum;
import com.quick.common.exception.QuickException;
import com.quick.common.config.LuaScriptConfig;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.utils.HttpServletUtil;
import com.quick.common.utils.IpUtil;
import com.quick.common.utils.RedisUtil;
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
 * @Description: Redis 限流切面类
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
        String combineKey = this.getCombineKey(rateLimiter, point);
        List<Object> keys = Collections.singletonList(combineKey);
        Long number = redisUtil.executeScript(limitScript, keys, count, time);
        if (number == null || number.intValue() > count) {
            throw new QuickException(ResponseEnum.SEND_MSG_FAST);
        }
        log.info("--------count:'{}',request:'{}', key:'{}'--------", count, number.intValue(), key);
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
