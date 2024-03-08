package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatSession;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 聊天会话（针对单聊） 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
public interface QuickChatSessionStore extends IService<QuickChatSession> {
    /**
     * 根据 account_id 查询会话列表
     *
     * @param accountId 账号id
     * @return 会话列表
     */
    List<QuickChatSession> getListByAccountId(String accountId);

    /**
     * 根据 session_id 删除会话
     *
     * @param sessionId 会话id
     * @return 执行结果
     */
    Boolean deleteBySessionId(Long sessionId);

    /**
     * 根据 from_id to_id 查询会话
     *
     * @param fromId 发送方
     * @param toId   接收方
     * @return 会话信息
     */
    QuickChatSession getByAccountId(String fromId, String toId);

    /**
     * 创建会话
     *
     * @param chatSession 会话信息
     * @return 执行结果
     */
    Boolean saveInfo(QuickChatSession chatSession);

    /**
     * 修改会话信息
     *
     * @param chatSession 会话信息
     * @return 执行结果
     */
    Boolean updateInfo(QuickChatSession chatSession);

    /**
     * 根据 account_id 集合查询会话列表
     *
     * @param fromIds 账户id列表
     * @param toId    账户id
     * @return 会话列表
     */
    List<QuickChatSession> getListByAccountIdList(List<String> fromIds, String toId);

    /**
     * 批量保存会话列表
     *
     * @param sessionPOList 会话列表
     * @return 执行结果
     */
    Boolean saveList(List<QuickChatSession> sessionPOList);

    /**
     * 查询未读数量
     *
     * @param loginAccountId 登录用户id
     * @param relationId     关联id
     * @param lastReadTime   最后读取时间
     * @return 未读数量
     */
    Integer getUnreadCount(String loginAccountId, String relationId, LocalDateTime lastReadTime);
}
