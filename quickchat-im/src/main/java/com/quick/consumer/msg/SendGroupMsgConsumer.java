package com.quick.consumer.msg;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.constant.RocketMQConstant;
import com.quick.enums.WsPushEnum;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.entity.WsPushEntity;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatGroupMemberStore;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2024/10/6 8:52
 * @Version 1.0
 * @Description: 发送信息（群聊）-消费者
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.SEND_CHAT_GROUP_MSG, consumerGroup = RocketMQConstant.CHAT_SEND_GROUP_ID)
public class SendGroupMsgConsumer implements RocketMQListener<String> {
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    public void onMessage(String message) {
        QuickChatMsg chatMsg = JSONUtil.parse(message).toBean(QuickChatMsg.class);
        List<QuickChatGroupMember> memberList = memberStore.getListByGroupId(chatMsg.getRelationId());
        for (QuickChatGroupMember member : memberList) {
            if (member.getAccountId().equals(chatMsg.getFromId())) {
                continue;
            }
            Channel channel = UserChannelRelation.getUserChannelMap().get(member.getAccountId());
            if (ObjectUtils.isNotEmpty(channel)) {
                WsPushEntity<QuickChatMsg> pushEntity = new WsPushEntity<>();
                pushEntity.setPushType(WsPushEnum.CHAT_MSG.getCode());
                pushEntity.setMessage(chatMsg);
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(chatMsg)));
            }
        }
    }
}
