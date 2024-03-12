package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatFriend;

import java.util.List;

/**
 * <p>
 * 聊天好友 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
public interface QuickChatFriendService extends IService<QuickChatFriend> {
    /**
     * 查询通讯录好友列表
     *
     * @return 好友列表信息
     */
    List<QuickChatFriend> getFriendList();

    /**
     * 添加好友
     *
     * @param accountId 账号id
     * @return 执行结果
     */
    Boolean addFriend(String accountId);

    /**
     * 删除好友
     *
     * @param accountId 账号id
     * @return 执行结果
     */
    Boolean deleteFriend(String accountId);

    /**
     * 拉黑好友
     *
     * @param accountId 账号id
     * @return 执行结果
     */
    Boolean blockFriend(String accountId);

    /**
     * 同意申请
     *
     * @param accountId 账号id
     * @return 执行结果
     */
    Boolean agreeApply(String accountId);

    /**
     * 根据 from_id，to_id 查询好友记录
     *
     * @param fromId
     * @param toId
     * @return 好友记录
     */
    QuickChatFriend getByFromIdAndToId(String fromId, String toId);
}
