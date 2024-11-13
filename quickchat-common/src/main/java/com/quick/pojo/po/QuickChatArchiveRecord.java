package com.quick.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author 徐志斌
 * @since 2024-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quick_chat_archive_record")
public class QuickChatArchiveRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 起始主键
     */
    @TableField("begin_id")
    private Long beginId;

    /**
     * 结束主键
     */
    @TableField("end_id")
    private Long endId;

    /**
     * 迁移数据总条数
     */
    @TableField("count")
    private Long count;

    /**
     * 状态
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
