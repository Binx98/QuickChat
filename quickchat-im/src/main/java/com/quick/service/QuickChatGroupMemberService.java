package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatUser;

import java.util.List;

/**
 * <p>
 * 群成员 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
public interface QuickChatGroupMemberService extends IService<QuickChatGroupMember> {
    /**
     * 根据 group_id 查询群成员
     *
     * @param groupId 群聊id
     * @return 群成员列表
     */
    List<QuickChatUser> getMemberByGroupId(String groupId);

    /**
     * 加入群聊
     *
     * @param groupId 群聊id
     * @return 执行结果
     */
    Boolean enterGroup(String groupId);
}
