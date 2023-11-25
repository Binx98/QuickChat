package com.quick.pojo.vo;

import lombok.Data;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 12:47
 * @Version 1.0
 * @Description:
 */
@Data
public class ChatSessionVO {
    /**
     * 账号id
     */
    private String accountId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 未读数量
     */
    private Integer unreadCount;
}
