package com.quick.strategy.msg.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quick.adapter.MsgAdapter;
import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ResponseEnum;
import com.quick.enums.SessionTypeEnum;
import com.quick.exception.QuickException;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import com.quick.utils.RelationUtil;
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
        Integer sessionType = msgDTO.getSessionType();
        String relationId = null;

        if (StringUtils.isBlank(content)) {
            throw new QuickException(ResponseEnum.FONT_MSG_IS_NULL);
        }

        if (SessionTypeEnum.SINGLE.getCode().equals(sessionType)) {
            relationId = RelationUtil.generate(fromId, toId);
        } else {
            relationId = toId;
        }
        QuickChatMsg chatMsg = MsgAdapter.buildChatMsgPO(fromId, toId, relationId,
                nickName, content, null, null, this.getEnum().getCode());
        msgStore.saveMsg(chatMsg);
        return chatMsg;
    }
}
