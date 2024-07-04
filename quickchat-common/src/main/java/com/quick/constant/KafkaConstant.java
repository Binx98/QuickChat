package com.quick.constant;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-20  18:15
 * @Description: Kafka 常量
 * @Version: 1.0
 */
public class KafkaConstant {
    /**
     * Kafka Topic 主题
     */
    public static final String SEND_CHAT_SINGLE_MSG = "SEND_CHAT_SINGLE_MSG";
    public static final String SEND_CHAT_GROUP_MSG = "SEND_CHAT_GROUP_MSG";
    public static final String SEND_CHAT_ENTERING = "SEND_CHAT_ENTERING";
    public static final String FRIEND_APPLY_TOPIC = "FRIEND_APPLY_TOPIC";
    public static final String GROUP_APPLY_TOPIC = "GROUP_APPLY_TOPIC";
    public static final String SYSTEM_NOTICE_TOPIC = "SYSTEM_NOTICE_TOPIC";

    /**
     * Kakfa 消费组
     */
    public static final String CHAT_SEND_GROUP_ID = "CHAT_SEND_GROUP_ID";
    public static final String APPLY_GROUP_ID = "CHAT_SEND_GROUP_ID";
}
