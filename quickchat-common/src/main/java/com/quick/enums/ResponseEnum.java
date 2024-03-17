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
    TOKEN_EXPIRE(502, "登录身份已过期，请重新登录！"),
    RELATION_GENERATE_ERROR(500, "生成 relation_id 失败，缺少关键信息！"),
    REQUEST_HOLDER_NO_ACCOUNT_ID(500, "全局请求上下文未获取到账号信息！"),
    SEND_MSG_FAST(500, "慢点发消息嘛，别急..."),
    CAN_NOT_RECALL(500, "信息发送已超过2分钟，不可撤回"),
    IS_YOUR_FRIEND(500, "对方已经是您的好友，不可重复添加"),
    GROUP_NOT_EXIST(500, "群组信息不存在！"),
    NOT_GROUP_OWNER(500, "您不是群主，不可进行当前操作！"),
    ;


    private Integer code;
    private String msg;
}