package com.quick.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;

import javax.annotation.PostConstruct;

/**
 * @Author 徐志斌
 * @Date: 2023/5/14 14:48
 * @Version 1.0
 * @Description: Kafka监听器
 */
@Slf4j
@Configuration
public class KafkaListener {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 监听Kafka执行情况
     */
    @PostConstruct
    private void listener() {
        kafkaTemplate.setProducerListener(new ProducerListener<String, Object>() {
            @Override
            public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
                log.info("-----------------------kafka ok,message:[{}]------------------------", producerRecord.value());
            }

            @Override
            public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                log.error("========================kafka error,message:[{}]========================", producerRecord.value());
            }
        });
    }
}

