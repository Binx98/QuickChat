package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatContact;

import java.util.List;

/**
 * <p>
 * 聊天好友 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
public interface QuickChatFriendContactStore extends IService<QuickChatContact> {
    /**
     * 根据 from_id 查询好友列表
     *
     * @param fromId 用户id
     * @return 好友列表
     */
    List<QuickChatContact> getListByFromId(String fromId);

    /**
     * 根据 from_id to_id 查询好友信息
     *
     * @param fromId 账号id
     * @param toId   账号id
     * @return 好友信息
     */
    QuickChatContact getByFromIdAndToId(String fromId, String toId);

    /**
     * 根据 from_id to_id 删除好友信息
     *
     * @param fromId 账号id
     * @param toId   账号id
     * @return 执行结果
     */
    Boolean deleteByFromIdAndToId(String fromId, String toId);
}
