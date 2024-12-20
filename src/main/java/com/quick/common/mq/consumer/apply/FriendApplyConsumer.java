package com.quick.common.mq.consumer.apply;

import cn.hutool.json.JSONUtil;
import com.quick.common.constant.RocketMQConstant;
import com.quick.common.enums.WsPushEnum;
import com.quick.common.netty.UserChannelRelation;
import com.quick.common.pojo.entity.WsPushEntity;
import com.quick.common.pojo.po.QuickChatApply;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-13  16:07
 * @Description: 好友申请-消费者
 * @Version: 1.0
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.FRIEND_APPLY_TOPIC, consumerGroup = RocketMQConstant.CHAT_SEND_GROUP_ID)
public class FriendApplyConsumer implements RocketMQListener<Message<QuickChatApply>> {
    @Override
    public void onMessage(Message<QuickChatApply> message) {
        QuickChatApply apply = message.getPayload();
        Channel channel = UserChannelRelation.getUserChannelMap().get(apply.getToId());
        if (ObjectUtils.isNotEmpty(channel)) {
            WsPushEntity<QuickChatApply> pushEntity = new WsPushEntity<>();
            pushEntity.setPushType(WsPushEnum.FRIEND_APPLY_NOTICE.getCode());
            pushEntity.setMessage(apply);
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
        }
    }
}
