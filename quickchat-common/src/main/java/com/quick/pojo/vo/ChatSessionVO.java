package com.quick.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 12:47
 * @Version 1.0
 * @Description: 聊天会话VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Long relationId;

    /**
     * 名称
     */
    private String sessionName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 未读数量
     */
    private Long unreadCount;

    /**
     * 在线状态（针对用户）
     */
    private Integer loginStatus;

    /**
     * 性别（针对用户）
     */
    private Integer gender;

    /**
     * 类型
     *
     * @see com.quick.enums.SessionTypeEnum
     */
    private Integer type;

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
