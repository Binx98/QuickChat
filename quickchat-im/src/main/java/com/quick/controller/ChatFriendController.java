package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import com.quick.service.QuickChatFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-21  10:08
 * @Description: 聊天好友
 * @Version: 1.0
 */
@RestController
@RequestMapping("/friend")
public class ChatFriendController {
    @Autowired
    private QuickChatFriendService friendService;

    /**
     * 查询好友列表
     */
    @GetMapping("/list")
    public R getFriendList() {
        friendService.getFriendList();
        return R.out(ResponseEnum.SUCCESS, null);
    }

    /**
     * 添加好友
     */
    @PostMapping("/add")
    public R addFriend() {
        friendService.addFriend();
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
        friendService.deleteFriend();
        return R.out(ResponseEnum.SUCCESS);
    }
}
