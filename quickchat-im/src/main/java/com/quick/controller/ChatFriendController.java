package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.po.QuickChatFriend;
import com.quick.response.R;
import com.quick.service.QuickChatFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<QuickChatFriend> friendList = friendService.getFriendList();
        return R.out(ResponseEnum.SUCCESS, friendList);
    }

    /**
     * 添加好友
     */
    @PostMapping("/add")
    public R addFriend(String accountId) {
        friendService.addFriend(accountId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 同意好友申请
     */
    @PostMapping("/agree")
    public R agreeApply(String accountId) {
        friendService.agreeApply(accountId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 拉黑好友
     */
    @PostMapping("/block")
    public R blockFriend(String accountId) {
        friendService.blockFriend(accountId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 删除好友
     */
    @DeleteMapping("/delete")
    public R deleteFriend(String accountId) {
        friendService.deleteFriend(accountId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 消息免打扰
     */

    /**
     * 设置备注
     */
}
