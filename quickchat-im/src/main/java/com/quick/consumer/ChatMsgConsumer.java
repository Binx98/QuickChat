package com.quick.consumer;

import cn.hutool.json.JSONUtil;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.constant.MQConstant;
import com.quick.constant.RedisConstant;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatSessionStore;
import com.quick.utils.RedissonLockUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 16:10
 * @Version 1.0
 * @Description: 聊天消息相关消费者
 */
@Component
public class ChatMsgConsumer {
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private RedissonLockUtil lockUtil;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 发送聊天消息
     */
    @KafkaListener(topics = MQConstant.CHAT_SEND_TOPIC, groupId = MQConstant.CHAT_SEND_GROUP_ID)
    public void sendChatMsg(String message) throws Throwable {
        // 处理通信双方聊天会话
        QuickChatMsg chatMsg = JSONUtil.parse(message).toBean(QuickChatMsg.class);
        String sendId = chatMsg.getSendId();
        String receiveId = chatMsg.getReceiveId();

        // 上锁：防止并发场景消息未读数量不准 FIXME 这个位置双方同时发送消息，根本锁不住（可以考虑加个字段锁key，用于控制双方会话）
        String lockKey = RedisConstant.UNREAD_LOCK_KEY + sendId + receiveId;
        lockUtil.executeWithLock(lockKey, 15, TimeUnit.SECONDS,
                () -> {
                    // 查询通信双方聊天会话信息
                    QuickChatSession chatSession1 = sessionStore.getOneByAccountId(sendId, receiveId);
                    QuickChatSession chatSession2 = sessionStore.getOneByAccountId(receiveId, sendId);

                    // 通讯会话不存在，创建会话未读数置为1
                    QuickChatSession chatSession = null;
                    if (ObjectUtils.isEmpty(chatSession1)) {
                        chatSession = ChatSessionAdapter.buildSessionPO(sendId, receiveId, 1);
                        sessionStore.saveInfo(chatSession);
                    }
                    if (ObjectUtils.isEmpty(chatSession2)) {
                        chatSession = ChatSessionAdapter.buildSessionPO(receiveId, sendId, 1);
                        sessionStore.saveInfo(chatSession);
                    }

                    // 通讯会话存在，会话未读数 + 1
                    chatSession1.setUnreadCount(chatSession1.getUnreadCount() + 1);
                    chatSession2.setUnreadCount(chatSession2.getUnreadCount() + 1);
                    sessionStore.updateInfo(chatSession1);
                    sessionStore.updateInfo(chatSession2);
                    return null;
                }
        );

        // 如果接收方建立了WebSocket连接，推送消息通知客户端
        taskExecutor.submit(() -> {
            Channel channel = UserChannelRelation.getUserChannelMap().get(chatMsg.getReceiveId());
            if (ObjectUtils.isNotEmpty(channel)) {
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(chatMsg)));
            }
        });
    }
}
