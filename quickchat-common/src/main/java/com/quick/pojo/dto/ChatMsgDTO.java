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
    private String fromId;

    /**
     * 接收方
     */
    private String toId;

    /**
     * 关联id
     */
    private Long relationId;

    /**
     * 发送人昵称
     */
    private String nickName;

    /**
     * 消息类型
     *
     * @see com.quick.enums.ChatMsgEnum
     */
    private Integer msgType;

    /**
     * 会话类型
     *
     * @see com.quick.enums.SessionTypeEnum
     */
    private Integer sessionType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息id：撤回消息、回复消息
     */
    private Long msgId;

    /**
     * 文件消息
     */
    private FileExtraDTO extraInfo;

    /**
     * 引用id
     */
    private Long quoteId;

}
