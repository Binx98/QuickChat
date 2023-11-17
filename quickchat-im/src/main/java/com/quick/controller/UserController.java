package com.quick.controller;


import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.LoginDTO;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.vo.UserVO;
import com.quick.response.R;
import com.quick.service.QuickUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
public class UserController {
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
    public R login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        userService.login(loginDTO, request);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    public R updateInfo() {
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 发送邮件
     */
    @PostMapping("/sendEmail")
    public R sendEmail() {
        return R.out(ResponseEnum.SUCCESS);
    }
}

