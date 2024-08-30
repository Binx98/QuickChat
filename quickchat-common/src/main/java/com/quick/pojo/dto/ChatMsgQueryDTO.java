package com.quick.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  15:15
 * @Description: 聊天消息查询DTO
 * @Version: 1.0
 */
@Data
public class ChatMsgQueryDTO {
    /**
     * 发送方
     */
    @NotNull(message = "发送方不能为空")
    private String accountId;
    /**
     * 接收方
     */
    @NotNull(message = "接收方不能为空")
    private String receiveId;
}
