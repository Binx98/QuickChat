package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
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
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 消息类型（1：文字，2：语音，3：表情包，4：文件，5：语音通话，6：视频通话）
     *
     * @see com.quick.enums.ChatMsgEnum
     */
    @TableField("msg_type")
    private Integer msgType;

    /**
     * 发送目标类型（1：单聊，2：群聊）
     */
    @TableField("goal_type")
    private Integer goalType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 删除标识
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

}
