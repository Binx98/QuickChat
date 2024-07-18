package com.quick.pojo.dto;

import lombok.Data;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-05-23  10:49
 * @Description: 群聊入参
 * @Version: 1.0
 */
@Data
public class GroupDTO {
    /**
     * 群组id
     */
    private Long groupId;

    /**
     * 群主账户id
     */
    private String accountId;

    /**
     * 群名
     */
    private String groupName;

    /**
     * 群头像
     */
    private String groupAvatar;
    /**
     * 群员邀请权限（0不可以，1可以）
     */
    private Integer invitePermission;

}
