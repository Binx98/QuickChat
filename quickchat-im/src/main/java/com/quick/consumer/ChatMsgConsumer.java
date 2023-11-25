package com.quick.consumer;

import cn.hutool.json.JSONUtil;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.constant.MQConstant;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatSessionStore;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

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
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 发送聊天消息
     */
    @KafkaListener(topics = MQConstant.CHAT_SEND_TOPIC, groupId = MQConstant.CHAT_SEND_GROUP_ID)
    public void sendChatMsg(Object message) {
        // 处理通信双方聊天会话
        QuickChatMsg chatMsg = (QuickChatMsg) message;
        String sendId = chatMsg.getSendId();
        String receiveId = chatMsg.getReceiveId();

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

        // 通讯会话存在，会话未读数 + 1（不想考虑并发问题，未读数不准就不准吧.....）


        // 如果接收方建立了WebSocket连接，推送消息通知客户端
        taskExecutor.submit(() -> {
            Channel channel = UserChannelRelation.getUserChannelMap().get(chatMsg.getReceiveId());
            if (ObjectUtils.isNotEmpty(channel)) {
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(chatMsg)));
            }
        });
    }
}
