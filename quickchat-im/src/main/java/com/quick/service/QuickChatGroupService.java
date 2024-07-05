package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.dto.GroupDTO;
import com.quick.pojo.po.QuickChatGroup;

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
     * 解散群聊
     *
     * @param groupId 群聊id
     * @return 执行结果
     */
    Boolean removeGroup(Long groupId);

    /**
     * 退出群聊
     *
     * @param groupId 群聊id
     * @return 执行结果
     */
    Boolean exitGroup(Long groupId);

    /**
     * 修改群组信息
     *
     * @param group 群组信息
     * @return 执行结果
     */
    Boolean updateInfo(GroupDTO group);
}
