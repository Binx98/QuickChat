package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 好友/群申请
 * </p>
 *
 * @author 徐志斌
 * @since 2024-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quick_chat_apply")
public class QuickChatApply implements Serializable {

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
     * 类型（1：好友，2：群聊）
     */
    @TableField("type")
    private Integer type;

    /**
     * 状态（0：未处理，1：已通过）
     */
    @TableField("status")
    private Integer status;

    /**
     * 删除标识
     */
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

}
