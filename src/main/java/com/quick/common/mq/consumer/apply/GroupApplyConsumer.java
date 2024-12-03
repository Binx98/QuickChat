package com.quick.common.mq.consumer.apply;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.common.constant.RocketMQConstant;
import com.quick.common.enums.WsPushEnum;
import com.quick.common.netty.UserChannelRelation;
import com.quick.common.pojo.entity.WsPushEntity;
import com.quick.common.pojo.po.QuickChatApply;
import com.quick.common.pojo.po.QuickChatGroupMember;
import com.quick.api.store.QuickChatGroupMemberStore;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-13  16:07
 * @Description: 群聊申请-消费者
 * @Version: 1.0
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.GROUP_APPLY_TOPIC, consumerGroup = RocketMQConstant.CHAT_SEND_GROUP_ID)
public class GroupApplyConsumer implements RocketMQListener<Message<QuickChatApply>> {
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    public void onMessage(Message<QuickChatApply> message) {
        QuickChatApply apply = message.getPayload();
        List<QuickChatGroupMember> members = memberStore.getListByGroupId(apply.getGroupId());
        for (QuickChatGroupMember member : members) {
            Channel channel = UserChannelRelation.getUserChannelMap().get(member.getAccountId());
            if (ObjectUtils.isNotEmpty(channel)) {
                WsPushEntity<QuickChatApply> pushEntity = new WsPushEntity<>();
                pushEntity.setPushType(WsPushEnum.GROUP_APPLY_NOTICE.getCode());
                pushEntity.setMessage(apply);
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
            }
        }
    }
}
