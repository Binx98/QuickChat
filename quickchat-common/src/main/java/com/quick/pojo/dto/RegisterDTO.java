package com.quick.pojo.dto;

import lombok.Data;

/**
 * @Author 徐志斌
 * @Date: 2023/11/14 21:12
 * @Version 1.0
 * @Description: 注册DTO
 */
@Data
public class RegisterDTO {
    /**
     * 账号
     */
    private String accountId;
    /**
     * 密码
     */
    private String password1;
    /**
     * 再次输入密码
     */
    private String password2;
    /**
     * 邮件
     */
    private String toEmail;
    /**
     * 邮箱验证码
     */
    private String emailCode;
}
