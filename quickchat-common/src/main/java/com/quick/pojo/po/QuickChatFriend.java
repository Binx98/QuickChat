package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 聊天好友
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quick_chat_friend")
public class QuickChatFriend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号id（发送方）
     */
    @TableField("from_id")
    private String fromId;

    /**
     * 账号id（接收方）
     */
    @TableField("to_id")
    private String toId;

    /**
     * 关联id
     */
    @TableField("relation_id")
    private String relationId;

    /**
     * 状态（0：未处理，1：已通过，2：未通过）
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
