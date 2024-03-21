package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatUser;
import com.quick.response.R;
import com.quick.service.QuickChatGroupMemberService;
import com.quick.service.QuickChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private QuickChatGroupService groupService;
    @Autowired
    private QuickChatGroupMemberService memberService;

    /**
     * 查询群聊列表
     */
    @GetMapping("/list")
    public R getGroupList() {
        List<QuickChatGroup> groups = groupService.getGroupList();
        return R.out(ResponseEnum.SUCCESS, groups);
    }

    /**
     * 查询群成员列表
     */
    @GetMapping("/member/{groupId}")
    public R getMemberList(@PathVariable String groupId) {
        List<QuickChatUser> userList = memberService.getMemberByGroupId(groupId);
        return R.out(ResponseEnum.SUCCESS, userList);
    }

    /**
     * 添加成员
     */
    @PostMapping("/enter/{groupId}")
    public R enterGroup(@PathVariable String groupId) {
        memberService.enterGroup(groupId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 移除成员
     */
    @DeleteMapping("/delete/{groupId}/{accountId}")
    public R exitGroup(@PathVariable String groupId, @PathVariable String accountId) {
        memberService.exitGroup(groupId, accountId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 创建群聊
     */
    @PostMapping("/create")
    public R createGroup(@RequestBody List<String> accountIds) {
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 解散群组
     */
    @DeleteMapping("/dismiss/{groupId}")
    public R dismiss(@PathVariable String groupId) {
        groupService.dismiss(groupId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 修改群聊信息
     */


    /**
     * 退出群聊
     */
}
