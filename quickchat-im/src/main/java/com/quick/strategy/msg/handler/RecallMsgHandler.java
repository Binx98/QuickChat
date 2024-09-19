package com.quick.strategy.msg.handler;

import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.file.handler.FileHandler;
import com.quick.strategy.file.handler.VoiceHandler;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-07  16:30
 * @Description: 撤回消息
 * @Version: 1.0
 */
@Component
public class RecallMsgHandler extends AbstractChatMsgStrategy {
    @Autowired
    private FileHandler fileHandler;
    @Autowired
    private VoiceHandler voiceHandler;
    @Autowired
    private QuickChatMsgStore msgStore;
    @Value("${quick-chat.no-recall-time}")
    private int noRecallTime;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.RECALL;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuickChatMsg sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 超过2分钟消息不可撤回
        QuickChatMsg chatMsg = msgStore.getByMsgId(msgDTO.getMsgId());
        if (ObjectUtils.isEmpty(chatMsg)) {
            throw new QuickException(ResponseEnum.FAIL);
        }
        if (chatMsg.getCreateTime().minusSeconds(noRecallTime).isBefore(LocalDateTime.now())) {
            throw new QuickException(ResponseEnum.CAN_NOT_RECALL);
        }

        // 针对文件消息：删除文件
        Integer msgType = msgDTO.getMsgType();
        if (ChatMsgEnum.VOICE.getCode().equals(msgType)) {
            fileHandler.deleteFile(msgDTO.getContent());
        } else if (ChatMsgEnum.FILE.getCode().equals(msgType)) {
            voiceHandler.deleteFile(msgDTO.getContent());
        }

        // 消息类型修改为撤回
        chatMsg.setMsgType(this.getEnum().getCode());
        msgStore.updateByMsgId(chatMsg);
        return chatMsg;
    }
}
