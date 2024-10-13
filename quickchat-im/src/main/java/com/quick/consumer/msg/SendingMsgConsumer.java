package com.quick.consumer.msg;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.constant.RocketMQConstant;
import com.quick.enums.WsPushEnum;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.entity.WsPushEntity;
import io.netty.channel.Channel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author 徐志斌
 * @Date: 2024/10/6 8:52
 * @Version 1.0
 * @Description: 消息发送中-消费者
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.SEND_CHAT_ENTERING, consumerGroup = RocketMQConstant.CHAT_SEND_GROUP_ID)
public class SendingMsgConsumer implements RocketMQListener<Message<Map<String, String>>> {
    @Override
    public void onMessage(Message<Map<String, String>> message) {
        Map<String, String> param = message.getPayload();
        Channel channel = UserChannelRelation.getUserChannelMap().get(param.get("toId"));
        if (ObjectUtils.isNotEmpty(channel)) {
            WsPushEntity<Map<String, String>> pushEntity = new WsPushEntity();
            pushEntity.setPushType(WsPushEnum.WRITING.getCode());
            pushEntity.setMessage(param);
            channel.writeAndFlush(pushEntity);
        }
    }
}
