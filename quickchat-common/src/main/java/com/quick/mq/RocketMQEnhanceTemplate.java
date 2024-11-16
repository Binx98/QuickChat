package com.quick.mq;

import com.alibaba.fastjson.JSONObject;
import com.quick.pojo.entity.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RocketMQEnhanceTemplate {
    @Autowired
    private RocketMQTemplate template;

    /**
     * 发送同步消息
     */
    public <T extends BaseMessage> SendResult syncSend(String topic, T message) {
        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        SendResult sendResult = template.syncSend(topic, sendMessage);
        log.info("[{}]同步消息[{}]发送结果[{}]", topic, JSONObject.toJSON(message), JSONObject.toJSON(sendResult));
        return sendResult;
    }


    /**
     * 发送异步消息
     */
    public <T extends BaseMessage> void asyncSend(String topic, T message) {
        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        template.asyncSend(topic, sendMessage, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("[asyncSend] msg send Success, topic:[{}], message: [{}], result: [{}]", topic, message, sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("[asyncSend] msg send Failed, topic:[{}], message: [{}]", topic, message);
            }
        });
    }
}
