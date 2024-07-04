package com.quick.aspect;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-09-13  16:22
 * @Description: 统计接口调用日志
 * @Version: 1.0
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {
    @Around("execution(* com.quick.controller..*.*(..))")
    public Object saveLog(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 过滤参数：ServletRequest,ServletResponse
        List<Object> paramList = Stream.of(joinPoint.getArgs())
                .filter(args -> !(args instanceof ServletRequest))
                .filter(args -> !(args instanceof ServletResponse))
                .collect(Collectors.toList());
        log.info("-------------method：[{}]，url：[{}]，params：[{}]-------------", method, uri, paramList);

        // 计算接口耗时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        long cost = stopWatch.getTotalTimeMillis();
        log.info("-------------url：[{}]，response：[{}]，time：[{}ms]-------------", uri, result, cost);
        return result;
    }
}
