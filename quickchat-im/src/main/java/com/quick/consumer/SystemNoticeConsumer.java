package com.quick.consumer;

import cn.hutool.json.JSONUtil;
import com.quick.constant.KafkaConstant;
import com.quick.enums.WsPushEnum;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.entity.WsPushEntity;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-13  16:07
 * @Description: 系统消息提醒消费者
 * @Version: 1.0
 */
@Component
public class SystemNoticeConsumer {
    /**
     * 系统通知：当前账户在别的地区登录
     */
    @KafkaListener(topics = KafkaConstant.SEND_CHAT_SINGLE_MSG, groupId = KafkaConstant.CHAT_SEND_GROUP_ID)
    public void sendChatMsg(String message) {
        Map<String, Object> param = JSONUtil.parse(message).toBean(Map.class);
        String accountId = (String) param.get("account_id");
        Channel channel = UserChannelRelation.getUserChannelMap().get(accountId);
        if (ObjectUtils.isNotEmpty(channel)) {
            WsPushEntity<Map<String, Object>> pushEntity = new WsPushEntity<>();
            pushEntity.setPushType(WsPushEnum.SYSTEM_NOTICE.getCode());
            pushEntity.setMessage(param);
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
        }
    }
}
