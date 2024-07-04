package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 好友申请
 * </p>
 *
 * @author 徐志斌
 * @since 2024-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quick_chat_friend_apply")
public class QuickChatFriendApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发起人
     */
    @TableField("from_id")
    private String fromId;

    /**
     * 接收人
     */
    @TableField("to_id")
    private String toId;

    /**
     * 申请信息
     */
    @TableField("apply_info")
    private String applyInfo;

    /**
     * 状态（0：未处理，1：已通过）
     */
    @TableField("status")
    private Boolean status;

    /**
     * 删除标识
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;


}
