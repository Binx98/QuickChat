package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.response.R;
import com.quick.service.QuickChatGroupMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-07-05  16:45
 * @Description: 群成员
 * @Version: 1.0
 */
@Api(tags = "群成员")
@RestController
@RequestMapping("/member")
public class QuickChatGroupMemberController {
    @Autowired
    private QuickChatGroupMemberService memberService;

    @ApiOperation("查询群成员")
    @PostMapping("/list")
    public R getMemberList(@NotNull(message = "群id参数不能为空") Long groupId) {
        List<ChatUserVO> members = memberService.getGroupMemberList(groupId);
        return R.out(ResponseEnum.SUCCESS, members);
    }

    @ApiOperation("添加成员")
    @GetMapping("/add")
    public R addMember(@NotNull(message = "群id参数不能为空") Long groupId,
                       @RequestBody List<String> accountIdList) {
        memberService.addMember(groupId, accountIdList);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("移除群成员")
    @GetMapping("/delete")
    public R deleteMember(@NotNull(message = "群id参数不能为空") Long groupId,
                          @NotBlank(message = "账号参数不能为空") String accountId) {
        memberService.deleteMember(groupId, accountId);
        return R.out(ResponseEnum.SUCCESS);
    }
}
