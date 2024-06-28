package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.response.R;
import com.quick.service.QuickChatFriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-21  10:08
 * @Description: 聊天好友
 * @Version: 1.0
 */
@Api(tags = "通讯录好友")
@RestController
@RequestMapping("/friend")
public class ChatFriendController {
    @Autowired
    private QuickChatFriendService friendService;

    @ApiOperation("查询好友列表")
    @PostMapping("/list")
    public R getFriendList() {
        List<ChatUserVO> result = friendService.getFriendList();
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("添加好友")
    @PostMapping("/add")
    public R addFriend(String toId) {
        friendService.addFriend(toId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * TODO 拉黑好友
     */

    /**
     * TODO 同意好友申请
     */

    /**
     * TODO 删除好友
     */

    /**
     * TODO 消息免打扰
     */

    /**
     * TODO 备注昵称
     */
}
