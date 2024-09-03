package com.quick.pojo.dto;

import lombok.Data;

/**
 * @Author 刘东辉
 * @Date 2024/7/8 17:32
 * @Description: 找回密码DTO
 * @Version: 1.0
 */
@Data
public class FindBackFormDTO {

    /**
     * 邮件
     */
    private String toEmail;

    /**
     * 邮箱验证码
     */
    private String emailCode;

    /**
     * 密码
     */
    private String password1;

    /**
     * 再次输入密码
     */
    private String password2;
}
