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
@TableName("quick_chat_friend_contact")
public class QuickChatContact implements Serializable {

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
     * 备注名称
     */
    @TableField("note_name")
    private String noteName;

    /**
     * 类型
     *
     * @see com.quick.enums.SessionTypeEnum
     */
    @TableField("type")
    private Integer type;

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
