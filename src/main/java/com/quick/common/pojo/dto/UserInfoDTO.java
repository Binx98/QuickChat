package com.quick.common.pojo.dto;

import lombok.Data;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-29  09:08
 * @Description: 账户信息DTO
 * @Version: 1.0
 */
@Data
public class UserInfoDTO {
    /**
     * 账号
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
     * 性别（0：女，1：男）
     */
    private Integer gender;
}
