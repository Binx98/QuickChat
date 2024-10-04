package com.quick.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2023/5/13 21:21
 * @Version 1.0
 * @Description: RocketMQ 生产者
 */
@Slf4j
@Component
public class RocketProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
