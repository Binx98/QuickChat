package com.quick.strategy.chatmsg.handler;

import com.quick.adapter.ChatMsgAdapter;
import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.QuickChatMsg;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.store.QuickChatMsgStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:02
 * @Description: 文字（表情）
 * @Version: 1.0
 */
@Component
public class FontHandler extends AbstractChatMsgStrategy {
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatMsgStore msgStore;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.FONT;
    }

    /**
     * 发送文字消息
     */
    @Override
    public void sendChatMsg(ChatMsgDTO msgDTO) {
        // 保存聊天记录信息（保存成功才是真正意义上发送成功）
        QuickChatMsg chatMsg = ChatMsgAdapter.buildChatMsgPO(msgDTO.getAccountId(),
                msgDTO.getReceiveId(), msgDTO.getContent(), ChatMsgEnum.FONT.getType());
        msgStore.saveMsg(chatMsg);

        // 查询最后一条聊天记录，超过三分钟，标记展示时间


        // 会话列表处理（未读数量 + 1）

        // 将消息发送到MQ
    }
}
