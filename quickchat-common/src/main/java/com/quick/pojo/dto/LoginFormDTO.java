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
    @NotBlank(message = "账号不能为空")
    private String accountId;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
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
