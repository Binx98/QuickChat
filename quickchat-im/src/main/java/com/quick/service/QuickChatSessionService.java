package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.vo.ChatSessionVO;
import com.quick.pojo.vo.UnreadCountVO;

import java.util.List;

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

    List<UnreadCountVO> getUnreadCountList(List<ChatSessionVO> sessionList);
}
