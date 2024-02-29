package com.quick.strategy.chatmsg.handler;

import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:07
 * @Description: 文件消息（图片、视频等）
 * @Version: 1.0
 */
@Component
public class FileHandler extends AbstractChatMsgStrategy {
    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.FILE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendChatMsg(ChatMsgDTO msgDTO) throws Throwable {

    }
}
