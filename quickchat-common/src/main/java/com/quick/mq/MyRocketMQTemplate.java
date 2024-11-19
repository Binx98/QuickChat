package com.quick.mq;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-11-19  11:05
 * @Description: 自定义 RocketMQTemplate
 * @Version: 1.0
 */
@Slf4j
@Component
public class MyRocketMQTemplate {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 同步消息
     *
     * @param topic   主题
     * @param message 消息实体
     * @return 执行结果
     */
    public <T> SendResult syncSend(String topic, T message) {
        Message<T> sendMessage = MessageBuilder.withPayload(message).build();
        SendResult sendResult = rocketMQTemplate.syncSend(topic, sendMessage);
        log.info("[{}]同步消息[{}]发送结果[{}]", topic, JSONObject.toJSON(message), JSONObject.toJSON(sendResult));
        return sendResult;
    }

    /**
     * 异步消息
     *
     * @param topic   主题
     * @param message 消息实体
     */
    public <T> void asyncSend(String topic, T message) {
        Message<T> sendMessage = MessageBuilder.withPayload(message).build();
        rocketMQTemplate.asyncSend(topic, sendMessage, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("[asyncSend] msg send Success, topic:[{}], message: [{}], result: [{}]",
                        topic, JSONObject.toJSON(message), JSONObject.toJSON(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("[asyncSend] msg send Failed, topic:[{}], message: [{}]",
                        topic, JSONObject.toJSON(message));
            }
        });
    }
}
