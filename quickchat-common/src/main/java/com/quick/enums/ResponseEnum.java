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

    USER_NOT_EXIST(50001, "该用户信息不存在"),
    ACCOUNT_ID_EXIST(50002, "该账号信息已存在，请勿重复注册！"),
    PASSWORD_DIFF(50003, "两次密码输入不一致，请重试！"),
    PASSWORD_ERROR(50003, "密码输入错误，请重试！"),
    VERIFY_CODE_EXPIRE(50004, "验证码信息已失效，请重试！"),
    VERIFY_CODE_ERROR(50004, "验证码输入错误，请重试！"),
    ;

    private Integer code;
    private String msg;
}