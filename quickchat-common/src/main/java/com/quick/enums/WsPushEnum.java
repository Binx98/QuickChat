package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-11  10:19
 * @Description: WebSocket 推送消息类型
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum WsPushEnum {
    SYSTEM_NOTICE(0, "系统通知：账号重复登录"),
    CHAT_MSG(1, "发送聊天消息"),

    FRIEND_APPLY_NOTICE(20, "好友申请"),
    GROUP_APPLY_NOTICE(21, "群聊申请"),

    ONLINE_STATUS(10, "登陆状态"),
    WRITING(11, "对方正在输入"),

    GROUP_RELEASE_NOTICE(30, "群内通知：解散群聊"),
    GROUP_ENTER_MEMBER_NOTICE(31, "群内通知：加入新成员"),
    ;

    private Integer code;
    private String msg;
}
