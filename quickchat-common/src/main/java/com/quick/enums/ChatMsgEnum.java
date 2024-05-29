package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  14:46
 * @Description: 聊天消息类型枚举
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum ChatMsgEnum {
    RECALL(0, "撤回"),
    FONT(1, "文字"),
    VOICE(2, "语音"),
    EMOJI(3, "表情包"),
    FILE(4, "文件（图片、视频）"),
    VOICE_CALL(5, "语音通话"),
    VIDEO_CALL(6, "视频通话"),
    AT(8, "艾特"),
    GROUP_NOTICE(10, "群通知，例如：加入群成员");

    private Integer code;
    private String msg;
}
