package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 22:00
 * @Version 1.0
 * @Description: 响应封装类状态码
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    USER_NOT_EXIST(500, "该用户信息不存在"),
    ACCOUNT_ID_EXIST(500, "该账号信息已存在，请勿重复注册！"),
    PASSWORD_DIFF(500, "两次密码输入不一致，请重试！"),
    PASSWORD_ERROR(500, "密码输入错误，请重试！"),
    IMG_CODE_ERROR(500, "图片验证码输入错误，请重试！"),
    EMAIL_CODE_EXPIRE(500, "邮箱验证码输入错误，请重试！"),
    EMAIL_CODE_ERROR(500, "邮箱验证码输入错误，请重试！"),
    TOKEN_EXPIRE(500, "Token身份信息已过期，请重新登录！"),
    REQUEST_HOLDER_NO_ACCOUNT_ID(500, "全局请求上下文未获取到账号信息！"),
    ;


    private Integer code;
    private String msg;
}