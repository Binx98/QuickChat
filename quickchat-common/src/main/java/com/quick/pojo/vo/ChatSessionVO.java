package com.quick.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

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
    /**
     * 在线状态
     */
    private String lineStatus;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 生日
     */
    private LocalDateTime birthDay;
}
