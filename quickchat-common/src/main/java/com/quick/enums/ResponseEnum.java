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

    USER_NOT_EXIST(500, "该用户信息不存在"),
    SESSION_INFO_ERROR(500, "聊天会话信息异常，请联系废物作者"),
    ACCOUNT_ID_EXIST(500, "该账号信息已存在，请勿重复注册！"),
    PASSWORD_DIFF(500, "两次密码输入不一致，请重试！"),
    PASSWORD_ERROR(500, "密码输入错误，请重试！"),
    IMG_CODE_ERROR(500, "图片验证码输入错误，请重试！"),
    EMAIL_CODE_EXPIRE(500, "邮箱验证码过期，请重试！"),
    EMAIL_CODE_ERROR(500, "邮箱验证码输入错误，请重试！"),
    EMAIL_HAS_REGISTERED(500, "邮箱已经被注册，请重试！"),
    EMAIL_NOT_REGISTERED(500, "邮箱尚未注册账号，请重试！"),
    TOKEN_EXPIRE(510, "登录身份已过期，请重新登录！"),
    RELATION_GENERATE_ERROR(500, "生成 relation_id 失败，缺少关键信息！"),
    REQUEST_HOLDER_NO_ACCOUNT_ID(500, "全局请求上下文未获取到账号信息！"),
    SEND_MSG_FAST(500, "慢点发消息嘛，别急..."),
    CAN_NOT_RECALL(500, "信息发送已超过2分钟，不可撤回"),
    IS_YOUR_FRIEND(500, "对方已经是您的好友，不可重复添加"),
    IS_NOT_YOUR_FRIEND(500, "对方不是您的好友，不可进行当前操作！"),
    GROUP_NOT_EXIST(500, "群组信息不存在！"),
    NOT_GROUP_OWNER(500, "您不是群主，不可进行当前操作！"),
    NOT_GROUP_MEMBER(500, "您不在该群组中，不可进行当前操作！"),
    GROUP_MEMBER_ADD_COUNT_NOT_ALLOW(500, "单次添加群成员数量不可超过20"),
    GROUP_SIZE_OVER(500, "群成员数量已满，不可以进入群聊"),
    FILE_OVER_SIZE(500, "文件大小不可超过：%s"),
    VOICE_TIME_NOT_ALLOW(500, "语音消息时长不可超过%s秒！"),
    FONT_MSG_IS_NULL(500, "输入框内容为空，不可进行发送"),
    ;

    @Setter
    private Integer code;
    @Setter
    private String msg;
}