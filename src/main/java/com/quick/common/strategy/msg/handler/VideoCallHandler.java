package com.quick.common.strategy.msg.handler;

import com.quick.common.enums.ChatMsgEnum;
import com.quick.common.pojo.dto.ChatMsgDTO;
import com.quick.common.pojo.po.QuickChatMsg;
import com.quick.common.strategy.msg.AbstractChatMsgStrategy;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:08
 * @Description: 视频通话消息
 * @Version: 1.0
 */
@Component
public class VideoCallHandler extends AbstractChatMsgStrategy {

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.VIDEO_CALL;
    }

    // TODO 视频电话
    @Override
    public QuickChatMsg sendMsg(ChatMsgDTO msgDTO) {
        return null;
    }
}
