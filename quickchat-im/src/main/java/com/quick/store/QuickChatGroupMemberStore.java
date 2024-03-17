package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatGroupMember;

import java.util.List;

/**
 * <p>
 * 群成员 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
public interface QuickChatGroupMemberStore extends IService<QuickChatGroupMember> {

    List<QuickChatGroupMember> getByGroupId(String groupId);

    Boolean enterGroup(QuickChatGroupMember memberPO);

    /**
     * 根据 group_id account_id 移出群聊
     *
     * @param groupId   群聊id
     * @param accountId 账号id
     * @return 执行结果
     */
    Boolean deleteByGroupIdAndAccountId(String groupId, String accountId);
}
