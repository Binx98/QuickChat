//package com.salt.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @Author 徐志斌
// * @Date: 2023/5/11 22:15
// * @Version 1.0
// * @Description: Redisson分布式锁工具类
// */
//@Slf4j
//@Component
//public class RedissonLockUtil {
//    @Autowired
//    private RedissonClient redissonClient;
//
//    /**
//     * 加锁：不设置时间默认30s，这里我默认设置为15s
//     */
//    public Boolean lock(String lockKey) {
//        try {
//            RLock lock = redissonClient.getLock(lockKey);
//            lock.lock(15, TimeUnit.SECONDS);
//            log.info("---------------------Thread [{}] RedissonLockUtil lock [{}] success----------------------",
//                    Thread.currentThread().getName(), lockKey);
//            return true;
//        } catch (Exception e) {
//            log.error("=================Redisson获取失败！线程：[{}]，lockKey：[{}]================",
//                    Thread.currentThread().getName(), lockKey);
//            return false;
//        }
//    }
//
//    /**
//     * 释放锁
//     */
//    public Boolean unlock(String lockKey) {
//        try {
//            RLock lock = redissonClient.getLock(lockKey);
//            lock.unlock();
//            log.info("--------------------Thread [{}] RedissonLockUtil unlock [{}] success--------------------",
//                    Thread.currentThread().getName(), lockKey);
//            return true;
//        } catch (Exception e) {
//            log.error("=================Redisson释放锁失败！线程：[{}]，lockKey：[{}]================",
//                    Thread.currentThread().getName(), lockKey);
//            return false;
//        }
//    }
//}