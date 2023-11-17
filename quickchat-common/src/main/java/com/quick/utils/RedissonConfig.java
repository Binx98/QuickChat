//package com.quick.utils;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Author: 徐志斌
// * @CreateTime: 2023-11-17  15:46
// * @Description: Redisson配置类
// * @Version: 1.0
// */
//@Configuration
//public class RedissonConfig {
//    @Autowired
//    private RedisProperties redisProperties;
//
//    @Bean
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        config.useSingleServer()
//                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
//                .setPassword(redisProperties.getPassword())
//                .setDatabase(redisProperties.getDatabase());
//        return Redisson.create(config);
//    }
//}
