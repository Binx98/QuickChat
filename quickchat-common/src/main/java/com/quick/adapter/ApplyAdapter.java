package com.quick.adapter;

import com.quick.pojo.po.QuickChatApply;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-07-05  13:35
 * @Description: 好友/群聊申请适配器
 * @Version: 1.0
 */
public class ApplyAdapter {
    public static QuickChatApply buildFriendApplyPO(String fromId, String toId,
                                                    String applyInfo, Integer type, Long groupId, Integer status) {
        QuickChatApply apply = new QuickChatApply();
        apply.setFromId(fromId);
        apply.setToId(toId);
        apply.setApplyInfo(applyInfo);
        apply.setType(type);
        apply.setGroupId(groupId);
        apply.setStatus(status);
        return apply;
    }
}
