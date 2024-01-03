package com.quick.controller;


import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.EmailDTO;
import com.quick.pojo.dto.LoginDTO;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.dto.UserUpdateDTO;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.UserVO;
import com.quick.response.R;
import com.quick.service.QuickUserService;
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
@RestController
@RequestMapping("/user")
public class ChatUserController {
    @Autowired
    private QuickUserService userService;

    /**
     * 查询用户信息
     */
    @GetMapping("/getByAccountId/{accountId}")
    public R getInfo(@PathVariable String accountId) throws Exception {
        UserVO result = userService.getByAccountId(accountId);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    /**
     * 根据 token 查询用户信息
     */
    @GetMapping("/getByToken")
    public R getByToken(String token) {
        QuickChatUser userInfo = userService.getByToken(token);
        return R.out(ResponseEnum.SUCCESS, userInfo);
    }

    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    public void captcha() throws IOException {
        userService.captcha();
    }

    /**
     * 注册账号
     */
    @PostMapping("/register")
    public R register(@RequestBody RegisterDTO registerDTO) throws Exception {
        userService.register(registerDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 登录账号
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginDTO loginDTO) throws Exception {
        String token = userService.login(loginDTO);
        return R.out(ResponseEnum.SUCCESS, token);
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    public R updateInfo(@RequestBody UserUpdateDTO userDTO) {
        userService.updateUser(userDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 发送邮件
     */
    @PostMapping("/sendEmail")
    public R sendEmail(@RequestBody EmailDTO emailDTO) throws Throwable {
        userService.sendEmail(emailDTO);
        return R.out(ResponseEnum.SUCCESS);
    }


    @GetMapping("/test")
    public R test() {
        return R.out(ResponseEnum.SUCCESS);
    }
}

