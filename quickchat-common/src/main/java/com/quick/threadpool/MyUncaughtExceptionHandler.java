package com.quick.threadpool;


import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-08-29  15:01
 * @Description: 线程池未捕获异常处理器
 * @Version: 1.0
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final MyUncaughtExceptionHandler instance = new MyUncaughtExceptionHandler();

    public static MyUncaughtExceptionHandler getInstance() {
        return instance;
    }

    /**
     * 作用：捕捉异常（线程池），打印日志
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("=========================线程池：[{}]，出现异常：[{}]=========================", t.getName(), e);
    }
}