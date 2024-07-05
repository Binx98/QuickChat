package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.response.R;
import com.quick.service.QuickChatFriendContactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-07-04  15:45
 * @Description: 通讯录-好友
 * @Version: 1.0
 */
@Api(tags = "通讯录-好友")
@RestController
@RequestMapping("/friend/contact")
public class QuickChatFriendContactController {
    @Autowired
    private QuickChatFriendContactService friendService;

    @ApiOperation("查询好友列表")
    @PostMapping("/list")
    public R getFriendList() {
        List<ChatUserVO> result = friendService.getFriendList();
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("添加好友")
    @PostMapping("/add")
    public R addFriend(String toId, String applyInfo) {
        friendService.addFriend(toId, applyInfo);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("删除好友")
    @PostMapping("/delete")
    public R deleteFriend(String toId) {
        friendService.deleteFriend(toId);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("拉黑好友")
    @PostMapping("/black")
    public R blackFriend() {
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("备注昵称")
    @PostMapping("/note")
    public R noteFriend() {
        return R.out(ResponseEnum.SUCCESS);
    }
}
