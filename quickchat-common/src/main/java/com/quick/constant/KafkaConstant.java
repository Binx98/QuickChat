package com.quick.constant;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-20  18:15
 * @Description: Kafka 常量
 * @Version: 1.0
 */
public class KafkaConstant {
    /**
     * 单聊、群聊发送
     */
    public static final String SEND_CHAT_SINGLE_MSG = "SEND_CHAT_SINGLE_MSG";
    public static final String SEND_CHAT_GROUP_MSG = "SEND_CHAT_GROUP_MSG";
    public static final String SEND_CHAT_ENTERING = "SEND_CHAT_ENTERING";

    /**
     * 申请通知发送
     */
    public static final String FRIEND_APPLY_TOPIC = "FRIEND_APPLY_TOPIC";
    public static final String GROUP_APPLY_TOPIC = "GROUP_APPLY_TOPIC";

    /**
     * 消费组
     */
    public static final String CHAT_SEND_GROUP_ID = "CHAT_SEND_GROUP_ID";
    public static final String APPLY_GROUP_ID = "CHAT_SEND_GROUP_ID";
}
