package com.quick.strategy.msg.handler;

import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 刘东辉
 * @CreateTime:
 * @Description: 艾特消息
 * @Version: 1.0
 */
@Component
public class AtMsgHandler extends AbstractChatMsgStrategy {
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
