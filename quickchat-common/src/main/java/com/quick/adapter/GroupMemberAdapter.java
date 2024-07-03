package com.quick.adapter;

import com.quick.pojo.po.QuickChatGroupMember;

/**
 * @Author 徐志斌
 * @Date: 2024/1/8 21:22
 * @Version 1.0
 * @Description: 群成员适配器
 */
public class GroupMemberAdapter {
    public static QuickChatGroupMember buildMemberPO(String groupId, String accountId) {
        QuickChatGroupMember groupMember = new QuickChatGroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setAccountId(accountId);
        return groupMember;
    }
}
