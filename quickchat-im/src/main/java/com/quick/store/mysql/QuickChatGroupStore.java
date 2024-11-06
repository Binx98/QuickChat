package com.quick.store.mysql;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatGroup;

import java.util.List;

/**
 * <p>
 * 群聊 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
public interface QuickChatGroupStore extends IService<QuickChatGroup> {
    /**
     * 根据 group_id 查询
     *
     * @param groupId 群组id
     * @return 群组信息
     */
    QuickChatGroup getByGroupId(Long groupId);

    /**
     * 修改群组信息
     *
     * @param group 群组信息
     * @return 执行结果
     */
    Boolean updateInfo(QuickChatGroup group);

    /**
     * 根据 group_id 列表批量查询群组信息列表
     *
     * @param groupIds 群组id列表
     * @return 群组信息列表
     */
    List<QuickChatGroup> getListByGroupIds(List<String> groupIds);

    /**
     * 根据 group_id 解散群聊
     *
     * @param groupId 群组id
     * @return 执行结果
     */
    Boolean dismissByGroupId(Long groupId);

    /**
     * 创建群聊
     *
     * @param groupPO 群聊信息实体
     * @return 执行结果
     */
    Boolean saveGroup(QuickChatGroup groupPO);
}
