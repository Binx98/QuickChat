package com.quick.strategy.chatmsg.handler;

import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:06
 * @Description: 表情包消息
 * @Version: 1.0
 */
public class EmojiHandler extends AbstractChatMsgStrategy {
    @Override
    protected ChatMsgEnum getEnum() {
        return null;
    }

    /**
     * 发送表情包消息
     */
    @Override
    public void sendChatMsg(ChatMsgDTO msgDTO) {

    }
}
