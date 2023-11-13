package com.salt.controller;


import com.salt.enums.ResponseEnum;
import com.salt.pojo.vo.UserVO;
import com.salt.response.R;
import com.salt.service.SaltUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public R getInfo(@PathVariable String accountId) {
        try {
            UserVO result = userService.getByAccountId(accountId);
            return R.out(ResponseEnum.SUCCESS, result);
        } catch (Exception e) {
            return R.out(ResponseEnum.FAIL, e.getMessage());
        }
    }
}

