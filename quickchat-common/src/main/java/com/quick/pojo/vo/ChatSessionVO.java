package com.quick.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
     * 主键id
     */
    private Long sessionId;
    /**
     * 发送方账号id
     */
    private String fromId;
    /**
     * 接收方账号id
     */
    private String toId;
    /**
     * 关联id
     */
    private String relationId;
    /**
     * 名称
     */
    private String sessionName;
    /**
     * 头像
     */
    private String sessionAvatar;
    /**
     * 未读数量
     */
    private Integer unreadCount;
    /**
     * 在线状态（针对用户）
     */
    private String lineStatus;
    /**
     * 性别（针对用户）
     */
    private Integer gender;
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
