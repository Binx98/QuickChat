package com.quick.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2023/5/13 21:21
 * @Version 1.0
 * @Description: Kafka生产者
 */
@Slf4j
@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 发送消息
     */
    public void send(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}
