package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 聊天会话（针对单聊）
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Data
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
    @TableField("send_id")
    private String sendId;

    /**
     * 账户id（接收者）
     */
    @TableField("receive_id")
    private String receiveId;

    /**
     * 消息未读数
     */
    @TableField("unread_count")
    private Integer unreadCount;

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
