package com.quick.controller;


import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.EmailDTO;
import com.quick.pojo.dto.LoginDTO;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.dto.UserUpdateDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.vo.UserVO;
import com.quick.response.R;
import com.quick.service.QuickUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    @GetMapping("/getInfo/{accountId}")
    public R getInfo(@PathVariable String accountId) throws Exception {
        UserVO result = userService.getByAccountId(accountId);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        userService.captcha(request, response);
    }

    /**
     * 注册账号
     */
    @PostMapping("/register")
    public R register(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) throws Exception {
        userService.register(registerDTO, request);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 登录账号
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = userService.login(loginDTO, request);
        return R.out(ResponseEnum.SUCCESS, resultMap);
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
     * <p>
     * TODO 这里使用策略模式，不同邮件对应不同的策略类
     */
    @PostMapping("/sendEmail")
    public R sendEmail(@RequestBody EmailDTO emailDTO) {
        userService.sendEmail(emailDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @GetMapping("/test")
    public String test() {
        QuickChatMsg build = QuickChatMsg.builder().receiveId("1111").sendId("123").build();
        return "接口整体通过";
    }
}

