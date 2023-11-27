package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-21  10:08
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/friend")
public class ChatFriendController {
    /**
     * 查询好友列表
     */
    @GetMapping("/list")
    public R getFriendList() {
        return R.out(ResponseEnum.SUCCESS, null);
    }

    /**
     * 添加好友
     */
    @PostMapping("/add")
    public R addFriend() {
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 处理好友申请状态（同意 or 拒绝）
     */
    @PostMapping("/")
    public R handleApply() {
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 删除好友
     */
    @DeleteMapping("/delete")
    public R deleteFriend() {
        return R.out(ResponseEnum.SUCCESS);
    }
}
