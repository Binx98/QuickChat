package com.quick.controller;


import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickValidationException;
import com.quick.pojo.dto.*;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.response.R;
import com.quick.service.QuickUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
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

    @ApiOperation("根据 account_id 查询用户信息")
    @GetMapping("/getByAccountId")
    public R getUserInfo(@NotBlank(message = "账号id参数不能为空") String accountId) throws Exception {
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
    public R login(@Validated @RequestBody LoginDTO loginDTO) throws Exception {
        Map<String, Object> result = userService.login(loginDTO);
        return R.out(ResponseEnum.SUCCESS, result);
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

    @ApiOperation("找回密码")
    @PutMapping("/findBack")
    public R findBack(@RequestBody UserFindBackDTO findBackDTO) throws Exception {
        userService.findBack(findBackDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("自定义业务异常校验Demo")
    @PostMapping("/validationDemo")
    public R validationDemo(LoginDTO loginDTO) throws Exception {
        if (StringUtils.isEmpty(loginDTO.getAccountId())) {
            throw new QuickValidationException("账户ID不可为空");
        }
        if (StringUtils.isEmpty(loginDTO.getPassWord())) {
            throw new QuickValidationException("密码不可为空");
        }
        return R.out(ResponseEnum.SUCCESS);
    }
}

