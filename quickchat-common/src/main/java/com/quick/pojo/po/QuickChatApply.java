package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 群聊id
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 状态（0：未处理，1：已通过）
     *
     * @see com.quick.enums.YesNoEnum
     */
    @TableField("status")
    private Integer status;

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
