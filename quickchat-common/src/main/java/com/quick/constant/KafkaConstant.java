package com.quick.constant;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-20  18:15
 * @Description: Kafka 常量
 * @Version: 1.0
 */
public class KafkaConstant {
    /**
     * 发送消息 Topic
     */
    public static final String SEND_CHAT_SINGLE_MSG = "SEND_CHAT_SINGLE_MSG";
    public static final String SEND_CHAT_GROUP_MSG = "SEND_CHAT_GROUP_MSG";

    /**
     * 好友、群组申请 Topic
     */
    public static final String FRIEND_APPLY_TOPIC = "FRIEND_APPLY_TOPIC";
    public static final String GROUP_APPLY_TOPIC = "GROUP_APPLY_TOPIC";

    /**
     * 群内通知 Topic
     */
    public static final String GROUP_ADD_MEMBER_NOTICE = "GROUP_ADD_MEMBER_NOTICE";
    public static final String GROUP_DELETE_MEMBER_NOTICE = "GROUP_DELETE_MEMBER_NOTICE";
    public static final String GROUP_RELEASE_NOTICE = "GROUP_RELEASE_NOTICE";

    /**
     * 对方正在输入 Topic
     */
    public static final String SEND_CHAT_ENTERING = "SEND_CHAT_ENTERING";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 消费组
     */
    public static final String CHAT_SEND_GROUP_ID = "CHAT_SEND_GROUP_ID";
    public static final String APPLY_GROUP_ID = "CHAT_SEND_GROUP_ID";
}
