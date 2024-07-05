package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.po.QuickChatGroupContact;
import com.quick.response.R;
import com.quick.service.QuickChatGroupContactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-21  10:05
 * @Description: 通讯录-群聊
 * @Version: 1.0
 */
@Api(tags = "通讯录-群聊")
@RestController
@RequestMapping("/group/contact")
public class QuickChatGroupContactController {
    @Autowired
    private QuickChatGroupContactService groupService;

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public R list() {
        List<QuickChatGroupContact> groupContacts = groupService.getGroupList();
        return R.out(ResponseEnum.SUCCESS, groupContacts);
    }
}
