package com.quick.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  15:15
 * @Description: 聊天消息DTO
 * @Version: 1.0
 */
@Data
public class ChatMsgDTO {
    /**
     * 消息id：撤回消息、回复消息
     */
    private Long msgId;

    /**
     * 发送方
     */
    private String fromId;

    /**
     * 接收方
     */
    private String toId;

    /**
     * 消息类型
     *
     * @see com.quick.enums.ChatMsgEnum
     */
    private Integer msgType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 额外信息：针对文件消息
     */
    private FileExtraDTO extraInfo;

    /**
     * 文件：针对语音
     */
    private MultipartFile file;
}
