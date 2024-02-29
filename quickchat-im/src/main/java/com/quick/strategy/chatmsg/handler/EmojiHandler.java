package com.quick.strategy.chatmsg.handler;

import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:06
 * @Description: 表情包消息
 * @Version: 1.0
 */
@Component
public class EmojiHandler extends AbstractChatMsgStrategy {
    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.EMOJI;
    }

    @Override
    public void sendChatMsg(ChatMsgDTO msgDTO) {

    }
}
