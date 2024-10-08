package com.quick.consumer.notice;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.constant.RocketMQConstant;
import com.quick.enums.WsPushEnum;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.entity.WsPushEntity;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
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
public class GroupReleaseConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        Map<String, Object> params = JSONUtil.parse(message).toBean(Map.class);
        List<String> accountIds = (List<String>) params.get("accountIds");
        Long groupId = (Long) params.get("groupId");
        for (String accountId : accountIds) {
            Channel channel = UserChannelRelation.getUserChannelMap().get(accountId);
            if (ObjectUtils.isNotEmpty(channel)) {
                WsPushEntity<Long> pushEntity = new WsPushEntity<>();
                pushEntity.setPushType(WsPushEnum.GROUP_RELEASE_NOTICE.getCode());
                pushEntity.setMessage(groupId);
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
            }
        }
    }
}
