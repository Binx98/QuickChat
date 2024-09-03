package com.quick.pojo.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Author 徐志斌
 * @Date: 2023/11/14 21:12
 * @Version 1.0
 * @Description: 注册DTO
 */
@Data
public class RegisterFormDTO {
    /**
     * 账号
     */
    @NotBlank(message = "发送方账号不能为空")
    @Length(min = 6, max = 15, message = "账号id长度是6-15位")
    private String accountId;

    /**
     * 昵称
     */
    @NotBlank(message = "发送方账号不能为空")
    @Length(min = 6, max = 15, message = "账号id长度是6-15位")
    private String nickName;

    /**
     * 性别
     *
     * @see com.quick.enums.GenderEnum
     */
    @NotBlank(message = "发送方账号不能为空")
    private Integer gender;

    /**
     * 密码
     */
    @NotBlank(message = "发送方账号不能为空")
    @Length(min = 6, max = 15, message = "账号id长度是6-15位")
    private String password1;

    /**
     * 再次输入密码
     */
    @NotBlank(message = "发送方账号不能为空")
    @Length(min = 6, max = 15, message = "账号id长度是6-15位")
    private String password2;

    /**
     * 邮件
     */
    @NotBlank(message = "发送方账号不能为空")
    @Length(min = 6, max = 15, message = "账号id长度是6-15位")
    private String toEmail;

    /**
     * 邮箱验证码
     */
    @NotBlank(message = "发送方账号不能为空")
    private String emailCode;

}
