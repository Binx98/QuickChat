package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-01-31  17:39
 * @Description: 微信登录
 * @Version: 1.0
 */
@RestController("/wx")
public class WeChatController {
    /**
     * 微信登录
     */
    @RequestMapping("/login")
    public R login() {
        String redirectUrl = "http://localhost:8888/wx/callback";
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 微信登陆回调
     */
    @GetMapping("/callback")
    public R callBack() {
        return R.out(ResponseEnum.SUCCESS);
    }
}
