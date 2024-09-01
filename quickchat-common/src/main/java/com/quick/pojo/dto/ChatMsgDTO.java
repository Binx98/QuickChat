package com.quick.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotBlank(message = "发送方不能为空")
    private String fromId;

    /**
     * 接收方
     */
    @NotBlank(message = "接收方不能为空")
    private String toId;

    /**
     * 关联id
     */
    @NotNull(message = "关联id不能为空")
    private Long relationId;

    /**
     * 发送人昵称
     */
    @NotBlank(message = "发送账号昵称不能为空")
    @Size(max = 20, message = "发送人昵称长度不能超过20字符")
    private String nickName;

    /**
     * 消息类型
     *
     * @see com.quick.enums.ChatMsgEnum
     */
    @NotNull(message = "消息类型不能为空")
    private Integer msgType;

    /**
     * 会话类型
     *
     * @see com.quick.enums.SessionTypeEnum
     */
    @NotNull(message = "会话类型不能为空")
    private Integer sessionType;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
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
