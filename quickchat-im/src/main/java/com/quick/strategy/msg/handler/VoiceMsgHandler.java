package com.quick.strategy.msg.handler;

import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:05
 * @Description: 语音消息
 * @Version: 1.0
 */
@Component
public class VoiceMsgHandler extends AbstractChatMsgStrategy {

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.VOICE;
    }

    @Override
    public void sendChatMsg(ChatMsgDTO msgDTO) {
        
    }
}
