package com.quick.consumer;

import cn.hutool.json.JSONUtil;
import com.quick.constant.MQConstant;
import com.quick.enums.ChatTypeEnum;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatGroupMemberStore;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 16:10
 * @Version 1.0
 * @Description: 聊天消息相关消费者
 */
@Component
public class ChatMsgConsumer {
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    /**
     * 通过Channel推送消息
     * 接收方建立了WebSocket连接，推送消息通知客户端
     */
    @KafkaListener(topics = MQConstant.SEND_CHAT_MSG, groupId = MQConstant.CHAT_SEND_GROUP_ID)
    public void sendChatMsg(String message) throws Throwable {
        // 解析 MQ 消息体
        QuickChatMsg chatMsg = JSONUtil.parse(message).toBean(QuickChatMsg.class);
        Integer goalType = chatMsg.getGoalType();
        String toId = chatMsg.getToId();

        // 1.单聊
        if (ChatTypeEnum.SINGLE.getType().equals(goalType)) {
            Channel channel = UserChannelRelation.getUserChannelMap().get(toId);
            if (ObjectUtils.isNotEmpty(channel)) {
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(chatMsg)));
            }
        }

        // 2.群聊
        if (ChatTypeEnum.GROUP.getType().equals(goalType)) {
            List<QuickChatGroupMember> memberList = memberStore.getByGroupId(toId);
            for (QuickChatGroupMember member : memberList) {
                Channel channel = UserChannelRelation.getUserChannelMap().get(member.getAccountId());
                if (ObjectUtils.isNotEmpty(channel)) {
                    channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(chatMsg)));
                }
            }
        }
    }
}
