package com.quick.pojo.dto;

import lombok.Data;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  15:15
 * @Description: 聊天消息DTO
 * @Version: 1.0
 */
@Data
public class ChatMsgDTO {
    /**
     * 发送方
     */
    private String accountId;
    /**
     * 接收方
     */
    private String receiveId;
    /**
     * 消息类型
     *
     * @see com.quick.enums.ChatMsgEnum
     */
    private Integer type;
    /**
     * 消息内容
     */
    private String content;
}
