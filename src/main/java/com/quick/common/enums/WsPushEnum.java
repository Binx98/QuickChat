package com.quick.common.enums;

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
    /**
     * 通用
     */
    SYSTEM_NOTICE(0, "系统通知：账号重复登录"),
    CHAT_MSG(1, "发送消息"),
    WRITING(2, "对方正在输入"),
    ONLINE_STATUS(3, "登陆状态"),

    /**
     * 好友、群聊申请
     */
    FRIEND_APPLY_NOTICE(20, "好友申请"),
    GROUP_APPLY_NOTICE(21, "群聊申请"),

    /**
     * 群聊内部通知
     */
    GROUP_RELEASE_NOTICE(30, "群内通知：解散群聊"),
    GROUP_ADD_MEMBER_NOTICE(31, "群内通知：加入新成员"),
    GROUP_DELETE_MEMBER_NOTICE(32, "群内通知：删除成员"),
    ;

    private Integer code;
    private String msg;
}
