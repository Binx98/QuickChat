package com.quick.strategy.chatmsg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.constant.MQConstant;
import com.quick.constant.RedisConstant;
import com.quick.enums.ChatMsgEnum;
import com.quick.kafka.KafkaProducer;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatMsgStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import com.quick.utils.AESUtil;
import com.quick.utils.RedissonLockUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
    @Autowired
    private QuickChatSessionStore sessionStore;

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
        QuickChatMsg chatMsg = ChatMsgAdapter.buildChatMsgPO(msgDTO.getAccountId(),
                msgDTO.getReceiveId(), msgDTO.getContent(), ChatMsgEnum.FONT.getType());
        msgStore.saveMsg(chatMsg);

        // 发送方id，接收方id
        String sendAccountId = chatMsg.getSendId();
        String receiveAccountId = chatMsg.getReceiveId();

        // 获取通讯双方会话锁Key（无论谁主动发送消息，都会生成相同锁KEY）
        String[] accountIdArr = {sendAccountId, receiveAccountId};
        Arrays.sort(accountIdArr);
        String sessionKey = accountIdArr[0] + accountIdArr[1];
        String lockKey = RedisConstant.UNREAD_LOCK_KEY + AESUtil.encrypt(sessionKey);

        // 上锁：防止并发场景消息未读数量不准
        lockUtil.executeWithLock(lockKey, 15, TimeUnit.SECONDS,
                () -> {
                    // 发送方
                    QuickChatSession chatSession1 = sessionStore.getOneByAccountId(sendAccountId, receiveAccountId);
                    if (ObjectUtils.isEmpty(chatSession1)) {
                        chatSession1 = ChatSessionAdapter.buildSessionPO(sendAccountId, receiveAccountId, 1);
                        sessionStore.saveInfo(chatSession1);
                    } else {
                        chatSession1.setUnreadCount(chatSession1.getUnreadCount() + 1);
                        sessionStore.updateInfo(chatSession1);
                    }

                    // 接收方
                    QuickChatSession chatSession2 = sessionStore.getOneByAccountId(receiveAccountId, sendAccountId);
                    if (ObjectUtils.isEmpty(chatSession2)) {
                        chatSession2 = ChatSessionAdapter.buildSessionPO(receiveAccountId, sendAccountId, 1);
                        return sessionStore.saveInfo(chatSession2);
                    } else {
                        chatSession2.setUnreadCount(chatSession2.getUnreadCount() + 1);
                        return sessionStore.updateInfo(chatSession2);
                    }
                }
        );

        // 将消息发送到MQ
        kafkaProducer.send(MQConstant.CHAT_SEND_TOPIC, JSONUtil.toJsonStr(chatMsg));
    }
}
