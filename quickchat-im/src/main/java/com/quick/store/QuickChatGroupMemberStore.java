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

    /**
     * 根据 group_id 查询群成员列表
     *
     * @param groupId 群聊id
     * @return 群成员列表
     */
    List<QuickChatGroupMember> getListByGroupId(String groupId);

    /**
     * 进入群聊
     *
     * @param memberPO 群成员信息
     * @return 执行结果
     */
    Boolean enterGroup(QuickChatGroupMember memberPO);

    /**
     * 根据 group_id account_id 移出群聊
     *
     * @param groupId   群聊id
     * @param accountId 账号id
     * @return 执行结果
     */
    Boolean deleteByGroupIdAndAccountId(String groupId, String accountId);

    /**
     * 根据 group_id 删除群成员列表
     *
     * @param groupId 群id
     * @return 执行结果
     */
    Boolean deleteByGroupId(String groupId);

    /**
     * 批量保存群成员
     *
     * @param memberList 群成员列表
     * @return 执行结果
     */
    Boolean saveMemberList(List<QuickChatGroupMember> memberList);
}
