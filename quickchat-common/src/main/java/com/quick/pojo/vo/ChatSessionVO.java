package com.quick.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 12:47
 * @Version 1.0
 * @Description: 聊天会话VO
 */
@Data
public class ChatSessionVO {
    /**
     * 目标账号id
     */
    private String toId;
    /**
     * 名称
     */
    private String name;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 在线状态（针对用户）
     */
    private String lineStatus;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 关联id
     */
    private String relationId;
    /**
     * 未读数量
     */
    private Integer unreadCount;
    /**
     * 最后已读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastReadTime;
    /**
     * 修改时间（发送消息）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
