package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 聊天信息
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("quick_chat_msg")
public class QuickChatMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账户id（发送人）
     */
    @TableField("from_id")
    private String fromId;

    /**
     * 账户id（接收人）
     */
    @TableField("to_id")
    private String toId;

    /**
     * 关联id
     */
    @TableField("relation_id")
    private String relationId;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 消息类型
     *
     * @see com.quick.enums.ChatMsgEnum
     */
    @TableField("msg_type")
    private Integer msgType;

    /**
     * 文件信息
     */
    @TableField("extra_info")
    private String extraInfo;

    /**
     * 时间展示（非空即展示）
     */
    @TableField("time_flag")
    private Integer timeFlag;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 删除标识
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

}
