package com.quick.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-05-23  10:49
 * @Description: 群聊入参
 * @Version: 1.0
 */
@Data
public class GroupDTO {
    /**
     * 群主id
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
     * 群成员
     */
    private List<String> accountIdList;
}
