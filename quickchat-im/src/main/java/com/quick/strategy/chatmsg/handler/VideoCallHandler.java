package com.quick.strategy.chatmsg.handler;

import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:08
 * @Description: 视频通话消息
 * @Version: 1.0
 */
public class VideoCallHandler extends AbstractChatMsgStrategy {

    @Override
    protected ChatMsgEnum getEnum() {
        return null;
    }

    /**
     * 发送视频通话消息
     */
    @Override
    public void sendChatMsg(ChatMsgDTO msgDTO) {

    }
}
