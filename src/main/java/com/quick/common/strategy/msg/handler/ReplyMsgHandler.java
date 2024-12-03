package com.quick.common.strategy.msg.handler;

import com.quick.common.enums.ChatMsgEnum;
import com.quick.common.pojo.dto.ChatMsgDTO;
import com.quick.common.pojo.po.QuickChatMsg;
import com.quick.api.store.QuickChatMsgStore;
import com.quick.common.strategy.msg.AbstractChatMsgStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 刘东辉
 * @CreateTime:
 * @Description: 引用、回复消息
 * @Version: 1.0
 */
@Component
public class ReplyMsgHandler extends AbstractChatMsgStrategy {
    @Autowired
    private QuickChatMsgStore msgStore;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.AT;
    }

    @Override
    public QuickChatMsg sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        return null;
    }
}
