package com.quick.consumer;

import cn.hutool.json.JSONUtil;
import com.quick.constant.MQConstant;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.po.QuickChatMsg;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 16:10
 * @Version 1.0
 * @Description: 聊天消息相关消费者
 */
@Component
public class ChatMsgConsumer {
    /**
     * 通过Channel推送消息
     * 接收方建立了WebSocket连接，推送消息通知客户端
     */
    @KafkaListener(topics = MQConstant.SEND_CHAT_MSG, groupId = MQConstant.CHAT_SEND_GROUP_ID)
    public void sendChatMsg(String message) throws Throwable {
        QuickChatMsg chatMsg = JSONUtil.parse(message).toBean(QuickChatMsg.class);
        Integer goalType = chatMsg.getGoalType();
        // 1.单聊
        if (goalType.equals(1)) {
            Channel channel = UserChannelRelation.getUserChannelMap().get(chatMsg.getFromId());
            if (ObjectUtils.isNotEmpty(channel)) {
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(chatMsg)));
            }
        }

        // 2.群聊
        else if (goalType.equals(2)) {
            // TODO 查询群成员
            String groupId = chatMsg.getFromId();
            Channel channel = UserChannelRelation.getUserChannelMap().get(null);
            if (ObjectUtils.isNotEmpty(channel)) {
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(chatMsg)));
            }
        }
    }
}
