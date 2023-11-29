package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-29  09:18
 * @Description: 表情包功能
 * @Version: 1.0
 */
@RestController
@RequestMapping("/emoji")
public class ChatEmojiController {
    /**
     * 查询该用户表情包列表
     */
    @GetMapping("/list")
    public R getEmojiList() {
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 添加表情包
     */
    @GetMapping("/add/{emojiUrl}")
    public R addEmoji(@PathVariable String emojiUrl) {
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 删除表情包
     */
    @GetMapping("/delete/{emojiId}")
    public R deleteEmoji(@PathVariable Long emojiId) {
        return R.out(ResponseEnum.SUCCESS);
    }
}
