package com.quick.common.strategy.msg.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quick.common.adapter.MsgAdapter;
import com.quick.common.enums.ChatMsgEnum;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.exception.QuickException;
import com.quick.common.pojo.dto.ChatMsgDTO;
import com.quick.common.pojo.po.QuickChatMsg;
import com.quick.api.store.QuickChatMsgStore;
import com.quick.common.strategy.msg.AbstractChatMsgStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:02
 * @Description: 文字消息
 * @Version: 1.0
 */
@Component
public class FontMsgHandler extends AbstractChatMsgStrategy {
    @Autowired
    private QuickChatMsgStore msgStore;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.FONT;
    }

    @Override
    public QuickChatMsg sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        String fromId = msgDTO.getFromId();
        String toId = msgDTO.getToId();
        String nickName = msgDTO.getNickName();
        String content = msgDTO.getContent();
        Long relationId = msgDTO.getRelationId();
        Long quoteId = msgDTO.getQuoteId();

        if (StringUtils.isBlank(content)) {
            throw new QuickException(ResponseEnum.FONT_MSG_NOT_EXIST);
        }

        QuickChatMsg chatMsg = MsgAdapter.buildChatMsgPO(fromId, toId, relationId,
                nickName, content, quoteId, null, this.getEnum().getCode());
        msgStore.saveMsg(chatMsg);
        return chatMsg;
    }
}
