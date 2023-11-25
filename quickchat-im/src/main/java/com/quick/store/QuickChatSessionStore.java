package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatSession;

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

    List<QuickChatSession> getListByAccountId(String accountId);

    Boolean updateUnreadBySessionId(Long sessionId, int count);

    Boolean deleteBySessionId(Long sessionId);
}
