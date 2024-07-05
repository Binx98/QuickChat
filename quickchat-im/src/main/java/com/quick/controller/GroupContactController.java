package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.po.QuickChatGroupContact;
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
 * @Description: 群聊
 * @Version: 1.0
 */
@Api(tags = "群聊相关")
@RestController
@RequestMapping("/group")
public class GroupContactController {
    @Autowired
    private QuickChatGroupService groupService;

    @ApiOperation("查询群成员")
    @PostMapping("/members")
    public R members(Long groupId) {
        List<ChatUserVO> list = groupService.getGroupMemberList(groupId);
        return R.out(ResponseEnum.SUCCESS, list);
    }

    @ApiOperation("添加成员")
    @GetMapping("/addMember")
    public R addMember(Long groupId, @RequestBody List<String> accountIdList) {
        groupService.addMember(groupId, accountIdList);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("移除群成员")
    @GetMapping("/removeMember")
    public R removeMember(Long groupId, String accountId) {
        groupService.removeMember(groupId, accountId);
        return R.out(ResponseEnum.SUCCESS);
    }
}
