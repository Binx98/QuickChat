package com.quick.common.adapter;

import com.quick.common.pojo.dto.GroupDTO;
import com.quick.common.pojo.po.QuickChatGroup;

/**
 * @Author 徐志斌
 * @Date: 2024/5/25 8:29
 * @Version 1.0
 * @Description: 群组适配器
 */
public class GroupAdapter {
    public static QuickChatGroup buildGroupPO(GroupDTO groupDTO) {
        QuickChatGroup group = new QuickChatGroup();
        group.setAccountId(groupDTO.getAccountId());
        group.setGroupName(groupDTO.getGroupName());
        return group;
    }
}
