package com.quick.common.pojo.dto;

import com.quick.common.enums.ChatMsgEnum;
import com.quick.common.enums.SessionTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


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
    private String nickName;

    /**
     * 消息类型
     *
     * @see ChatMsgEnum
     */
    @NotNull(message = "消息类型不能为空")
    private Integer msgType;

    /**
     * 会话类型
     *
     * @see SessionTypeEnum
     */
    @NotNull(message = "会话类型不能为空")
    private Integer sessionType;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @Length(max = 20, message = "输入框内容长度不能超过300字符")
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
