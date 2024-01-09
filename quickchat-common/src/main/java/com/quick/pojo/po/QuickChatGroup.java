package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 群聊
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
     * 群组id
     */
    @TableId(value = "group_id", type = IdType.ASSIGN_ID)
    private String groupId;

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
     * 群成员数量
     */
    @TableField("member_count")
    private Integer memberCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;


}
