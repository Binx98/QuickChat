package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

    // --------------------账户---------------------
    ACCOUNT_ID_NOT_EXIST(500, "该用户信息不存在"),
    ACCOUNT_ID_EXIST(500, "该账号信息已存在，请勿重复注册！"),
    PASSWORD_DIFF(500, "两次密码输入不一致，请重试！"),
    PASSWORD_ERROR(500, "密码输入错误，请重试！"),
    IMG_CODE_ERROR(500, "图片验证码输入错误，请重试！"),
    EMAIL_CODE_ERROR(500, "邮箱验证码输入错误，请重试！"),
    EMAIL_HAS_REGISTERED(500, "邮箱已经被注册，请重试！"),
    EMAIL_NOT_REGISTERED(500, "邮箱尚未注册账号，请重试！"),
    TOKEN_EXPIRE(510, "登录身份已过期，请重新登录！"),

    // --------------------会话---------------------
    SESSION_INFO_ERROR(500, "聊天会话信息异常，请联系废物作者"),
    SESSION_NOT_EXIST(500, "聊天会话信息不存在，请重新加载后操作"),

    // --------------------聊天消息---------------------
    FILE_OVER_SIZE(500, "文件大小不可超过：%s"),
    VOICE_TIME_NOT_ALLOW(500, "语音消息时长不可超过%s秒！"),
    FONT_MSG_NOT_EXIST(500, "输入框内容为空，不可进行发送"),
    CAN_NOT_RECALL(500, "信息发送已超过2分钟，不可撤回"),
    SEND_MSG_FAST(500, "慢点发消息嘛，别急..."),


    // --------------------通讯录---------------------
    IS_YOUR_FRIEND(500, "对方已经是您的好友，不可重复添加"),
    IS_NOT_YOUR_FRIEND(500, "对方不是您的好友，不可进行当前操作！"),

    // --------------------群组---------------------
    GROUP_NOT_EXIST(500, "群组信息不存在！"),
    NOT_GROUP_OWNER(500, "您不是群主，不可进行当前操作！"),
    NOT_GROUP_MEMBER(500, "您不在该群组中，不可进行当前操作！"),
    GROUP_MEMBER_ADD_COUNT_NOT_ALLOW(500, "单次添加群成员数量不可超过20"),
    GROUP_MEMBER_COUNT_NOT_EXIST(500, "添加群成员参数不能为空！"),
    GROUP_MEMBER_NOT_ALLOW(500, "群成员不可进行当前操作"),
    GROUP_SIZE_OVER(500, "群成员数量已满，不可以进入群聊"),


    // --------------------系统申请---------------------
    APPLY_NOT_EXIST(500, "该申请信息不存在"),
    APPLY_IS_FINISH(500, "该申请信息已处理，请勿继续操作"),
    ;

    @Setter
    private Integer code;
    @Setter
    private String msg;
}