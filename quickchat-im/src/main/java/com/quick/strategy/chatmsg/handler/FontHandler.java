package com.quick.strategy.chatmsg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.constant.MQConstant;
import com.quick.enums.ChatMsgEnum;
import com.quick.kafka.KafkaProducer;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import com.quick.utils.RedissonLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
    public void sendChatMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 保存聊天记录信息（保存成功 = 发送成功）
        String sendAccountId = msgDTO.getAccountId();
        String receiveAccountId = msgDTO.getReceiveId();
        QuickChatMsg chatMsg = ChatMsgAdapter.buildChatMsgPO(sendAccountId,
                receiveAccountId, msgDTO.getContent(), ChatMsgEnum.FONT.getType());
        msgStore.saveMsg(chatMsg);

        // 上锁：防止并发场景消息未读数量不准
        String sessionLockKey = this.generateSessionLockKey(sendAccountId, receiveAccountId);
        lockUtil.executeWithLock(sessionLockKey, 15, TimeUnit.SECONDS,
                () -> {
                    return this.handleSession(chatMsg.getFromId(), chatMsg.getToId());
                }
        );


        // 推送Kafka：聊天信息同步ElasticSearch、通过Channel推送消息
        kafkaProducer.send(MQConstant.SYNC_CHAT_MSG_ES, JSONUtil.toJsonStr(chatMsg));
        kafkaProducer.send(MQConstant.SEND_CHAT_MSG, JSONUtil.toJsonStr(chatMsg));
    }
}