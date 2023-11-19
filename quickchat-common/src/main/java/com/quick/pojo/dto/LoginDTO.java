package com.quick.pojo.dto;

import lombok.Data;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  15:15
 * @Description: 登录入参DTO
 * @Version: 1.0
 */
@Data
public class LoginDTO {
    /**
     * 账号
     */
    private String accountId;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 图片验证码
     */
    private String imgCode;
}
