package com.quick.api.controller;

import com.quick.common.enums.ResponseEnum;
import com.quick.common.pojo.vo.ChatUserVO;
import com.quick.common.response.R;
import com.quick.api.service.QuickChatContactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-07-04  15:45
 * @Description: 通讯录（好友 + 群组）
 * @Version: 1.0
 */
@Api(tags = "通讯录")
@RestController
@RequestMapping("/contact")
public class QuickChatContactController {
    @Autowired
    private QuickChatContactService contactService;

    @ApiOperation("查询列表")
    @PostMapping("/list")
    public R getFriendList() {
        List<ChatUserVO> result = contactService.getContactList();
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("添加好友")
    @PostMapping("/add/{accountId}")
    public R addFriend(@PathVariable String accountId, String applyInfo) {
        contactService.addFriend(accountId, applyInfo);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("删除好友")
    @PostMapping("/delete/{accountId}")
    public R deleteFriend(@PathVariable String accountId) {
        contactService.deleteFriend(accountId);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("备注昵称")
    @PostMapping("/note/{accountId}/{noteName}")
    public R noteFriend(@PathVariable String accountId,
                        @PathVariable String noteName) {
        contactService.noteFriend(accountId, noteName);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("拉黑好友")
    @PostMapping("/black")
    public R blackFriend() {
        return R.out(ResponseEnum.SUCCESS);
    }
}
