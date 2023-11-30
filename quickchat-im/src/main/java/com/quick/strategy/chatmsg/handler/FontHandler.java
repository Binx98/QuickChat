package com.quick.strategy.chatmsg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.constant.MQConstant;
import com.quick.enums.ChatMsgEnum;
import com.quick.kafka.KafkaProducer;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.store.QuickChatMsgStore;
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
    private QuickChatMsgStore msgStore;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.FONT;
    }

    /**
     * 发送文字消息
     */
    @Override
    public void sendChatMsg(ChatMsgDTO msgDTO) {
        // 保存聊天记录信息（保存成功才算发送成功）
        QuickChatMsg chatMsg = ChatMsgAdapter.buildChatMsgPO(msgDTO.getAccountId(),
                msgDTO.getReceiveId(), msgDTO.getContent(), ChatMsgEnum.FONT.getType());
        msgStore.saveMsg(chatMsg);

        // 将消息发送到MQ
        kafkaProducer.send(MQConstant.CHAT_SEND_TOPIC, JSONUtil.toJsonStr(chatMsg));
    }
}
