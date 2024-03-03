package com.quick.strategy.chatmsg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.constant.MQConstant;
import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ChatTypeEnum;
import com.quick.kafka.KafkaProducer;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.dto.ExtraInfoDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import com.quick.utils.RedissonLockUtil;
import com.quick.utils.RelationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:02
 * @Description: 文字、Emoji
 * @Version: 1.0
 */
@Component
public class FontHandler extends AbstractChatMsgStrategy {
    @Autowired
    private RedissonLockUtil lockUtil;
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.FONT;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendChatMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 保存聊天信息
        String fromId = msgDTO.getFromId();
        String toId = msgDTO.getToId();
        String content = msgDTO.getContent();
        QuickChatMsg chatMsg = ChatMsgAdapter.buildChatMsgPO(fromId, toId, content, null, ChatMsgEnum.FONT.getType());
        msgStore.saveMsg(chatMsg);

        // 处理双方会话信息
        String relationId = RelationUtil.generate(fromId, toId);
        QuickChatSession chatSession = lockUtil.executeWithLock(relationId, 15, TimeUnit.SECONDS,
                () -> this.handleSession(fromId, toId)
        );

        // 通过Channel推送消息（单聊、群聊）
        if (ChatTypeEnum.SINGLE.getType().equals(chatSession.getType())) {
            kafkaProducer.send(MQConstant.SEND_CHAT_SINGLE_MSG, JSONUtil.toJsonStr(chatMsg));
        }
        if (ChatTypeEnum.GROUP.getType().equals(chatSession.getType())) {
            kafkaProducer.send(MQConstant.SEND_CHAT_GROUP_MSG, JSONUtil.toJsonStr(chatMsg));
        }
    }
}
