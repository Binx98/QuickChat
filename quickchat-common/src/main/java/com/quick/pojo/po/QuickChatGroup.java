package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 群组信息
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quick_chat_group")
public class QuickChatGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 群id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 群主id
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 群名
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 群头像
     */
    @TableField("group_avatar")
    private String groupAvatar;

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
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;


}
