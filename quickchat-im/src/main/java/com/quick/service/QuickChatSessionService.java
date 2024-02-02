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

    List<ChatSessionVO> getSessionList();

    Boolean deleteSession(Long sessionId);

    Boolean updateLastReadTime(Long sessionId);

    Map<String, Integer> getUnreadCountList( List<QuickChatSession> sessionList);
}
