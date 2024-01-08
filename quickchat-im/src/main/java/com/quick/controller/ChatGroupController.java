package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-21  10:05
 * @Description: 群聊相关
 * @Version: 1.0
 */
@RestController
@RequestMapping("/group")
public class ChatGroupController {
    /**
     * 查询群成员
     */
    @GetMapping("/member")
    public R getMemberList() {
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 查询用户群聊列表
     */

    /**
     * 创建群组
     */

    /**
     * 修改群组
     */

    /**
     * 解散群组
     */
}
