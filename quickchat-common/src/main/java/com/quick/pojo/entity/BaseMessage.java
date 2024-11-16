package com.quick.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-11-16  10:26
 * @Description: MQ消息实体，所有消息都需要继承此类
 * @Version: 1.0
 */
@Data
public abstract class BaseMessage {
    /**
     * 业务键，用于RocketMQ控制台查看消费情况
     */
    protected String key;

    /**
     * 发送消息来源，用于排查问题
     */
    protected String source = "";

    /**
     * 发送时间
     */
    protected LocalDateTime sendTime = LocalDateTime.now();

    /**
     * 重试次数，用于判断重试次数，超过重试次数发送异常警告
     */
    protected Integer retryTimes = 0;
}
