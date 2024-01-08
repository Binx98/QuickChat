package com.quick.adapter;

import com.quick.pojo.po.QuickChatGroupMember;

/**
 * @Author 徐志斌
 * @Date: 2024/1/8 21:22
 * @Version 1.0
 * @Description: GroupMemberAdapter
 */
public class GroupMemberAdapter {
    public static QuickChatGroupMember buildMemberPO(Long groupId, String accountId) {
        return QuickChatGroupMember.builder()
                .groupId(groupId)
                .accountId(accountId)
                .build();
    }
}
