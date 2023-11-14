//package com.salt.controller;
//
//import com.salt.enums.ResponseEnum;
//import com.salt.response.R;
//import com.salt.utils.RedisUtil;
//import com.salt.utils.RedissonLockUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @Author 徐志斌
// * @Date: 2023/11/14 21:58
// * @Version 1.0
// * @Description: TestController
// */
//@RestController
//public class TestController {
//    @Autowired
//    private RedisUtil redisUtil;
//    @Autowired
//    private RedissonLockUtil lockUtil;
//
//    @RequestMapping("/test")
//    public R test() {
//        lockUtil.lock("111122223333");
//        return null;
//    }
//}
