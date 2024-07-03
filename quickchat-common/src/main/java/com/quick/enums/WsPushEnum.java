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
    CHAT_MSG(1, "聊天消息"),
    FRIEND_APPLY(2, "好友申请通知"),
    GROUP_NOTICE(3, "群内通知"),
    ONLINE_STATUS(10, "登陆状态"),
    WRITING(11, "对方正在输入"),
    ;

    private Integer code;
    private String msg;
}
