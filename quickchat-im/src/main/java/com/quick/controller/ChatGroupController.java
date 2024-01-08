package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.po.QuickChatUser;
import com.quick.response.R;
import com.quick.service.QuickChatGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-21  10:05
 * @Description: 群聊相关
 * @Version: 1.0
 */
@RestController
@RequestMapping("/group")
public class ChatGroupController {
    @Autowired
    private QuickChatGroupMemberService memberService;

    /**
     * 查询群成员列表
     */
    @GetMapping("/member/{groupId}")
    public R getMemberList(@PathVariable Long groupId) {
        List<QuickChatUser> userList = memberService.getMemberByGroupId(groupId);
        return R.out(ResponseEnum.SUCCESS, userList);
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
