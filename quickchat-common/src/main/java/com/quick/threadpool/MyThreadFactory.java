package com.quick.threadpool;

import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-08-29  16:38
 * @Description: 自定义线程工厂
 * @Version: 1.0
 */
@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {
    private final ThreadFactory factory;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = factory.newThread(r);
        thread.setUncaughtExceptionHandler(MyUncaughtExceptionHandler.getInstance());
        return thread;
    }
}
