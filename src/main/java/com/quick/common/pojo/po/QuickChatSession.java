package com.quick.common.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.quick.common.enums.SessionTypeEnum;
import com.quick.common.enums.YesNoEnum;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 聊天会话
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
@TableName("quick_chat_session")
public class QuickChatSession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账户id（发送者）
     */
    @TableField("from_id")
    private String fromId;

    /**
     * 账户id（接收者）
     */
    @TableField("to_id")
    private String toId;

    /**
     * 会话名称
     */
    @TableField("session_name")
    private String sessionName;

    /**
     * 关联id
     * 单聊：雪花算法
     * 群聊：群id
     */
    @TableField("relation_id")
    private Long relationId;

    /**
     * 会话类型（1：单聊，2：群聊）
     *
     * @see SessionTypeEnum
     */
    @TableField("type")
    private Integer type;

    /**
     * 会话状态（0：未删除，1：已删除）
     *
     * @see YesNoEnum
     */
    @TableField("status")
    private Integer status;

    /**
     * 置顶标识（0：未置顶，1：置顶）
     *
     * @see YesNoEnum
     */
    @TableField("top_flag")
    private Integer topFlag;

    /**
     * 最后读取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("last_read_time")
    private LocalDateTime lastReadTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

}
