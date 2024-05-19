package com.quick.controller;


import com.quick.annotation.RateLimiter;
import com.quick.enums.LimitTypeEnum;
import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.EmailDTO;
import com.quick.pojo.dto.LoginDTO;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.dto.UserUpdateDTO;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.response.R;
import com.quick.service.QuickUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
public class ChatUserController {
    @Autowired
    private QuickUserService userService;

    @ApiOperation("根据 account_id 查询用户信息")
    @GetMapping("/getByAccountId/{accountId}")
    public R getInfo(@PathVariable String accountId) throws Exception {
        ChatUserVO result = userService.getByAccountId(accountId);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("根据 token 查询用户信息")
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
    public R register(@RequestBody RegisterDTO registerDTO) throws Exception {
        userService.register(registerDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("登录账号")
    @PostMapping("/login")
    public R login(@RequestBody LoginDTO loginDTO) throws Exception {
        String token = userService.login(loginDTO);
        return R.out(ResponseEnum.SUCCESS, token);
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/update")
    public R updateInfo(@RequestBody UserUpdateDTO userDTO) {
        userService.updateUser(userDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("发送邮件")
    @PostMapping("/sendEmail")
    public R sendEmail(@RequestBody EmailDTO emailDTO) throws Throwable {
        userService.sendEmail(emailDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("测试")
    @GetMapping("/test")
    @RateLimiter(key = "test", time = 30, count = 3, limitType = LimitTypeEnum.IP)
    public R test() {
        return R.out(ResponseEnum.SUCCESS);
    }
}

