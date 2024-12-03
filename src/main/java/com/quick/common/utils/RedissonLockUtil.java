package com.quick.common.utils;

import com.quick.common.enums.ResponseEnum;
import com.quick.common.exception.QuickException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author 徐志斌
 * @Date: 2023/11/9 22:15
 * @Version 1.0
 * @Description: Redisson 分布式锁工具类
 */
@Slf4j
@Component
public class RedissonLockUtil {
    @Autowired
    private RedissonClient redissonClient;

    public <T> T executeWithLock(String key, int waitTime, TimeUnit unit, SupplierThrow<T> supplier) throws Throwable {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock(waitTime, unit);
        if (!lockSuccess) {
            throw new QuickException(ResponseEnum.FAIL);
        }
        try {
            return supplier.get();
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @FunctionalInterface
    public interface SupplierThrow<T> {
        T get() throws Throwable;
    }
}
