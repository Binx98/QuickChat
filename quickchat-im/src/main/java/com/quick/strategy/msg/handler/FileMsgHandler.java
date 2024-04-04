package com.quick.strategy.msg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.dto.FileExtraDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:07
 * @Description: 文件消息（图片、视频等）
 * @Version: 1.0
 */
@Component
public class FileMsgHandler extends AbstractChatMsgStrategy {
    @Autowired
    private QuickChatMsgStore msgStore;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.FILE;
    }

    @Override
    public QuickChatMsg sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        String fromId = msgDTO.getFromId();
        String toId = msgDTO.getToId();
        String fileUrl = msgDTO.getContent();
        FileExtraDTO extraInfo = msgDTO.getExtraInfo();
        QuickChatMsg chatMsg = ChatMsgAdapter.buildChatMsgPO(fromId, toId,
                fileUrl, JSONUtil.toJsonStr(extraInfo), this.getEnum().getCode());
        msgStore.saveMsg(chatMsg);
        return chatMsg;
    }
}
