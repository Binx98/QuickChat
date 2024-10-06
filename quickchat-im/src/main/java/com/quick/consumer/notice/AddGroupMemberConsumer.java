package com.quick.consumer.notice;

import com.quick.constant.RocketMQConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2024/10/6 14:42
 * @Version 1.0
 * @Description: AddGroupMemberConsumer
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.GROUP_ADD_MEMBER_NOTICE, consumerGroup = RocketMQConstant.CHAT_SEND_GROUP_ID)
public class AddGroupMemberConsumer {

}
