package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.GroupDTO;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.response.R;
import com.quick.service.QuickChatGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-21  10:05
 * @Description: 群聊相关
 * @Version: 1.0
 */
@Api(tags = "群聊相关")
@RestController
@RequestMapping("/group")
public class ChatGroupController {
    @Autowired
    private QuickChatGroupService groupService;

    @ApiOperation("创建群聊")
    @PostMapping("/create")
    public R create(@RequestBody GroupDTO group) {
        groupService.createGroup(group);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("查询群成员")
    @PostMapping("/members")
    public R members(Long groupId) {
        List<ChatUserVO> list = groupService.getGroupMemberList(groupId);
        return R.out(ResponseEnum.SUCCESS, list);
    }

    /**
     * TODO 查询群聊列表
     */
    @ApiOperation("查询群聊列表")
    @GetMapping("/list")
    public R list(String accountId) {
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * TODO 添加成员
     */

    /**
     * TODO 移除成员
     */


    /**
     * TODO 解散群组
     */

    /**
     * TODO 修改群组信息
     */

    /**
     * TODO 修改群成员昵称
     */

    /**
     * TODO 退出群聊
     */
}
