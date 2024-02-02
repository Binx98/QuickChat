package com.quick.constant;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-20  18:15
 * @Description: 消息队列 MQ 常量
 * @Version: 1.0
 */
public class MQConstant {
    /**
     * 主题
     */
    public static final String SEND_CHAT_SINGLE_MSG = "SEND_CHAT_SINGLE_MSG";
    public static final String SEND_CHAT_GROUP_MSG = "SEND_CHAT_GROUP_MSG";
    public static final String SYNC_DB_TO_ES = "SYNC_DB_TO_ES";

    /**
     * 消费组
     */
    public static final String CHAT_SEND_GROUP_ID = "CHAT_SEND_GROUP_ID";
}
