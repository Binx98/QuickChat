package com.quick.strategy.chatmsg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.constant.MQConstant;
import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ChatTypeEnum;
import com.quick.kafka.KafkaProducer;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import com.quick.utils.RedissonLockUtil;
import com.quick.utils.RelationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:02
 * @Description: 文字（表情）
 * @Version: 1.0
 */
@Component
public class FontHandler extends AbstractChatMsgStrategy {
    @Autowired
    private RedissonLockUtil lockUtil;
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
    public void sendChatMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 保存聊天记录信息（保存成功 = 发送成功）
        QuickChatMsg chatMsg = this.saveChatMsg(msgDTO);

        // 上锁：防止并发场景会话重复创建问题
        String relationId = RelationUtil.generate(chatMsg.getFromId(), chatMsg.getToId());
        QuickChatSession chatSession = lockUtil.executeWithLock(relationId, 15, TimeUnit.SECONDS,
                () -> this.handleSession(chatMsg.getFromId(), chatMsg.getToId())
        );

        // 通过Channel推送消息
        if (ChatTypeEnum.SINGLE.getType().equals(chatSession.getType())) {
            kafkaProducer.send(MQConstant.SEND_CHAT_MSG, JSONUtil.toJsonStr(chatMsg));
        } else {
            kafkaProducer.send(MQConstant.SEND_CHAT_GROUP_MSG, JSONUtil.toJsonStr(chatMsg));
        }

        // 聊天信息同步ElasticSearch
        kafkaProducer.send(MQConstant.SYNC_CHAT_MSG_ES, JSONUtil.toJsonStr(chatMsg));
    }
}
