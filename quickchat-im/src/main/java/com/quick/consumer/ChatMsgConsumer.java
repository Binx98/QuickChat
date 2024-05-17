package com.quick.consumer;

import cn.hutool.json.JSONUtil;
import com.quick.constant.MQConstant;
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
import java.util.Map;

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
     * 对方正在输入
     */
    @KafkaListener(topics = MQConstant.SEND_CHAT_ENTERING, groupId = MQConstant.CHAT_SEND_GROUP_ID)
    public void entering(String message) {
        Map<String, String> param = JSONUtil.parse(message).toBean(Map.class);
        String fromId = param.get("fromId");
        String toId = param.get("toId");
        Channel channel = UserChannelRelation.getUserChannelMap().get(toId);
        if (ObjectUtils.isNotEmpty(channel)) {
            channel.writeAndFlush(new TextWebSocketFrame(fromId));
        }
    }

    /**
     * 单聊 Channel 推送
     */
    @KafkaListener(topics = MQConstant.SEND_CHAT_SINGLE_MSG, groupId = MQConstant.CHAT_SEND_GROUP_ID)
    public void sendChatMsg(String message) {
        QuickChatMsg chatMsg = JSONUtil.parse(message).toBean(QuickChatMsg.class);
        Channel channel = UserChannelRelation.getUserChannelMap().get(chatMsg.getToId());
        if (ObjectUtils.isNotEmpty(channel)) {
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(chatMsg)));
        }
    }

    /**
     * 群聊 Channel 推送
     */
    @KafkaListener(topics = MQConstant.SEND_CHAT_GROUP_MSG, groupId = MQConstant.CHAT_SEND_GROUP_ID)
    public void sendChatMsgToGroup(String message) {
        QuickChatMsg chatMsg = JSONUtil.parse(message).toBean(QuickChatMsg.class);
        List<QuickChatGroupMember> memberList = memberStore.getByGroupId(chatMsg.getToId());
        for (QuickChatGroupMember member : memberList) {
            Channel channel = UserChannelRelation.getUserChannelMap().get(member.getAccountId());
            if (ObjectUtils.isNotEmpty(channel)) {
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(chatMsg)));
            }
        }
    }
}
