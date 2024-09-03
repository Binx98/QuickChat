package com.quick.pojo.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  15:15
 * @Description: 登录入参DTO
 * @Version: 1.0
 */
@Data
public class LoginFormDTO {
    /**
     * 账号
     */
    @Length(min = 6, max = 15, message = "账号长度是6-15位")
    private String accountId;

    /**
     * 密码
     */
    @Length(min = 8, max = 20, message = "密码长度是8-20位")
    private String passWord;

    /**
     * 图片验证码
     */
    @NotBlank(message = "图片验证码不能为空")
    private String verifyCode;

    /**
     * 记住我
     */
    private Boolean rememberPwd;
}
