package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 通讯录
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quick_chat_contact")
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
     * 类型
     *
     * @see com.quick.enums.SessionTypeEnum
     */
    @TableField("type")
    private Integer type;

    /**
     * 备注
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
