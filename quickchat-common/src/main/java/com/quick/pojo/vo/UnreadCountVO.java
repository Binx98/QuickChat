package com.quick.pojo.vo;

import lombok.Data;

/**
 * @Author 徐志斌
 * @Date: 2024/2/4 21:07
 * @Version 1.0
 * @Description: 未读数量VO
 */
@Data
public class UnreadCountVO {
    /**
     * 关联id
     */
    private String relationId;
    /**
     * 未读数量
     */
    private Integer unreadCount;
}
