package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-11  10:19
 * @Description: WebSocket推送消息类型
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum WsPushEnum {
    SYS_MSG(0, "系统通知"),
    CHAT_MSG(1, "聊天信息"),
    FRIEND_APPLY(2, "好友申请"),
    ONLINE_STATUS(3, "登陆状态"),
    GROUP_NOTICE(4, "群组通知"),
    ;

    private Integer code;
    private String msg;
}
