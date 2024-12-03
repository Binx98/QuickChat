package com.quick.common.mq.consumer.notice;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.common.constant.RocketMQConstant;
import com.quick.common.enums.WsPushEnum;
import com.quick.common.netty.UserChannelRelation;
import com.quick.common.pojo.entity.WsPushEntity;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author 徐志斌
 * @Date: 2024/10/6 14:42
 * @Version 1.0
 * @Description: 解散群组-消费者
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.GROUP_RELEASE_NOTICE, consumerGroup = RocketMQConstant.CHAT_SEND_GROUP_ID)
public class GroupReleaseConsumer implements RocketMQListener<Message<Map<String, Object>>> {

    @Override
    public void onMessage(Message<Map<String, Object>> message) {
        Map<String, Object> params = message.getPayload();
        List<String> accountIds = (List<String>) params.get("accountIds");
        for (String accountId : accountIds) {
            Channel channel = UserChannelRelation.getUserChannelMap().get(accountId);
            if (ObjectUtils.isNotEmpty(channel)) {
                WsPushEntity<Long> pushEntity = new WsPushEntity<>();
                pushEntity.setPushType(WsPushEnum.GROUP_RELEASE_NOTICE.getCode());
                pushEntity.setMessage((Long) params.get("groupId"));
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
            }
        }
    }
}
