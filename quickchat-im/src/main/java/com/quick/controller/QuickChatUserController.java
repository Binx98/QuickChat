package com.quick.controller;


import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.*;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.response.R;
import com.quick.service.QuickUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
@Api(tags = "用户相关")
@RestController
@RequestMapping("/user")
public class QuickChatUserController {
    @Autowired
    private QuickUserService userService;

    @ApiOperation("查询用户信息（根据账号）")
    @GetMapping("/getByAccountId/{accountId}")
    public R getUserInfo(@PathVariable String accountId) throws Exception {
        ChatUserVO result = userService.getByAccountId(accountId);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("查询用户信息（根据Token）")
    @GetMapping("/getByToken")
    public R getByToken() {
        QuickChatUser userInfo = userService.getByToken();
        return R.out(ResponseEnum.SUCCESS, userInfo);
    }

    @ApiOperation("图片验证码")
    @GetMapping("/captcha")
    public void captcha() throws IOException {
        userService.captcha();
    }

    @ApiOperation("注册账号")
    @PostMapping("/register")
    public R register(@Validated @RequestBody RegisterFormDTO registerDTO) throws Exception {
        userService.register(registerDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("登录账号")
    @PostMapping("/login")
    public R login(@Validated @RequestBody LoginFormDTO loginDTO) throws Exception {
        Map<String, Object> result = userService.login(loginDTO);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/update")
    public R updateInfo(@Validated @RequestBody UserInfoDTO userDTO) {
        userService.updateUser(userDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("发送邮件")
    @PostMapping("/sendEmail")
    public R sendEmail(@Validated @RequestBody EmailDTO emailDTO) throws Throwable {
        userService.sendEmail(emailDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("找回密码")
    @PutMapping("/findBack")
    public R findBack(@Validated @RequestBody FindBackFormDTO findBackDTO) throws Exception {
        userService.findBack(findBackDTO);
        return R.out(ResponseEnum.SUCCESS);
    }
}

