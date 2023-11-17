package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:35
 * @Version 1.0
 * @Description: 聊天相关
 */
@RestController("/chat")
public class ChatController {
    /**
     * 发送消息
     */
    @PostMapping("/send")
    public R sendMessage() {
        return R.out(ResponseEnum.SUCCESS);
    }
}
