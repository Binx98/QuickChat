package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.vo.ChatSessionVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 聊天会话（针对单聊） 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
public interface QuickChatSessionService extends IService<QuickChatSession> {
    /**
     * 查询会话列表
     *
     * @return 会话列表信息
     */
    List<ChatSessionVO> getSessionList();

    /**
     * 根据 sessionId 删除会话
     *
     * @param sessionId 会话主键id
     * @return 执行结果
     */
    Boolean deleteSession(Long sessionId);

    /**
     * 更新最后会话读取时间
     *
     * @param sessionId 会话id
     * @return 执行结果
     */
    Boolean updateLastReadTime(Long sessionId);

    /**
     * 获取会话未读数
     *
     * @param sessionList 会话列表
     * @return 未读数结果Map
     */
    Map<String, Integer> getUnreadCountMap(List<ChatSessionVO> sessionList);

    /**
     * 查询会话信息
     *
     * @param fromId 发送方
     * @param toId   接收方
     * @return 会话信息
     */
    ChatSessionVO getSessionInfo(String fromId, String toId);

    /**
     * 置顶会话
     *
     * @param sessionId
     * @return 执行结果
     */
    Boolean topSession(Long sessionId);
}
