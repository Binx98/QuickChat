package com.quick.config;

import com.quick.config.mq.ClientConfigFactory;
import com.quick.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.MQAdminImpl;
import org.apache.rocketmq.client.impl.MQClientManager;
import org.apache.rocketmq.client.impl.factory.MQClientInstance;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.TopicConfig;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * RocketMQ应用启动自动创建Topic
 *
 * @author : Barry.chen
 * @date : 2024-10-19
 **/
@Slf4j
@Component
public class RocketMQInitializer {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQInitializer.class);


    @Value("${rocketmq.name-server}")
    private String nameServer;

    @PostConstruct
    public void createTopic() {
//        try {
//            ClientConfig clientConfig = ClientConfigFactory.createClientConfig(
//                    nameServer,
//                    "myClientInstance",
//                    "192.168.0.1"
//            );
//
//            MQClientInstance mqClientInstance = MQClientManager.getInstance().getOrCreateMQClientInstance(clientConfig);
//            MQAdminImpl mqAdmin = new MQAdminImpl(mqClientInstance);
            DefaultMQProducer producer = new DefaultMQProducer("springboot_producer_group");
            producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");

//            // 尝试创建Topic，如果失败则记录错误信息
//            try {
//                mqAdmin.createTopic(RocketMQConstant.SEND_CHAT_SINGLE_MSG, RocketMQConstant.SEND_CHAT_SINGLE_MSG, 1);
//                // ... 创建其他Topic
//            } catch (MQClientException e) {
//                log.error(e.getMessage());
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
    }
//
//    @PostConstruct
//    public void createTopic() throws Exception {
//        // 使用工厂模式创建ClientConfig对象
//        ClientConfig clientConfig = ClientConfigFactory.createClientConfig(
//                nameServer,
//                "myClientInstance",
//                "192.168.0.1"
//        );
//
//
//        MQClientInstance mqClientInstance = MQClientManager.getInstance().getOrCreateMQClientInstance(clientConfig);
//
//        MQAdminImpl mqAdmin = new MQAdminImpl(mqClientInstance);
//        mqAdmin.createTopic(RocketMQConstant.SEND_CHAT_SINGLE_MSG, RocketMQConstant.SEND_CHAT_SINGLE_MSG, 1);
//        mqAdmin.createTopic(RocketMQConstant.SEND_CHAT_GROUP_MSG, RocketMQConstant.SEND_CHAT_GROUP_MSG, 1);
//        mqAdmin.createTopic(RocketMQConstant.FRIEND_APPLY_TOPIC, RocketMQConstant.FRIEND_APPLY_TOPIC, 1);
//        mqAdmin.createTopic(RocketMQConstant.GROUP_APPLY_TOPIC, RocketMQConstant.GROUP_APPLY_TOPIC, 1);
//        mqAdmin.createTopic(RocketMQConstant.GROUP_ADD_MEMBER_NOTICE, RocketMQConstant.GROUP_ADD_MEMBER_NOTICE, 1);
//        mqAdmin.createTopic(RocketMQConstant.GROUP_DELETE_MEMBER_NOTICE, RocketMQConstant.GROUP_DELETE_MEMBER_NOTICE, 1);
//        mqAdmin.createTopic(RocketMQConstant.GROUP_RELEASE_NOTICE, RocketMQConstant.GROUP_RELEASE_NOTICE, 1);
//        mqAdmin.createTopic(RocketMQConstant.SEND_CHAT_ENTERING, RocketMQConstant.SEND_CHAT_ENTERING, 1);
//        mqAdmin.createTopic(RocketMQConstant.CHAT_SEND_GROUP_ID, RocketMQConstant.CHAT_SEND_GROUP_ID, 1);
//        mqAdmin.createTopic(RocketMQConstant.APPLY_GROUP_ID, RocketMQConstant.APPLY_GROUP_ID, 1);
//
//    }
}