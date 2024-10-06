package com.quick.consumer.msg;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.constant.RocketMQConstant;
import com.quick.enums.WsPushEnum;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.entity.WsPushEntity;
import com.quick.pojo.po.QuickChatMsg;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2024/10/6 8:52
 * @Version 1.0
 * @Description: 发送单聊信息-消费者
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.SEND_CHAT_SINGLE_MSG, consumerGroup = RocketMQConstant.CHAT_SEND_GROUP_ID)
public class SendFriendMsgConsumer {
    public void sendFriendMsg(String message) {
        QuickChatMsg chatMsg = JSONUtil.parse(message).toBean(QuickChatMsg.class);
        Channel channel = UserChannelRelation.getUserChannelMap().get(chatMsg.getToId());
        if (ObjectUtils.isNotEmpty(channel)) {
            WsPushEntity<QuickChatMsg> pushEntity = new WsPushEntity<>();
            pushEntity.setPushType(WsPushEnum.CHAT_MSG.getCode());
            pushEntity.setMessage(chatMsg);
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
        }
    }
}
