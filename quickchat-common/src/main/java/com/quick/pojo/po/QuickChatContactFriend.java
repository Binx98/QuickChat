package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 通讯录-好友
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quick_chat_contact_friend")
public class QuickChatContactFriend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号id
     */
    @TableField("from_id")
    private String fromId;

    /**
     * 账号id
     */
    @TableField("to_id")
    private String toId;

    /**
     * 备注名称（好友专用）
     */
    @TableField("note_name")
    private String noteName;

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
