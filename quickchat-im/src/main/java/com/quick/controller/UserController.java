package com.quick.controller;


import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.vo.UserVO;
import com.quick.response.R;
import com.quick.service.SaltUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private SaltUserService userService;

    /**
     * 查询用户信息
     */
    @GetMapping("/getInfo/{accountId}")
    public R getInfo(@PathVariable String accountId) throws Exception {
        UserVO result = userService.getByAccountId(accountId);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    /**
     * 注册账号
     */
    @PostMapping("/register")
    public R register(RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 登录账号
     */

    /**
     * 修改用户信息
     */
}

