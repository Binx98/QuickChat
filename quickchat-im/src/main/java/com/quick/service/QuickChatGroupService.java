package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.dto.GroupDTO;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.vo.ChatUserVO;

import java.util.List;

/**
 * <p>
 * 群聊 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
public interface QuickChatGroupService extends IService<QuickChatGroup> {
    /**
     * 创建群聊
     *
     * @param group 群聊参数
     * @return 执行结果
     */
    Boolean createGroup(GroupDTO group);

    /**
     * 查询群成员列表
     *
     * @param groupId 群id
     * @return 群成员列表
     */
    List<ChatUserVO> getGroupMemberList(Long groupId);

    /**
     * 添加群成员
     *
     * @param groupId       群组id
     * @param accountIdList 账户id列表
     * @return 执行结果
     */
    Boolean addMember(Long groupId, List<String> accountIdList);

    /**
     * 移除群成员
     *
     * @param groupId   群组id
     * @param accountId 账户id
     * @return 执行结果
     */
    Boolean removeMember(Long groupId, String accountId);

    /**
     * 解散群聊
     *
     * @param groupId 群聊id
     * @return 执行结果
     */
    Boolean removeGroup(Long groupId);
}
