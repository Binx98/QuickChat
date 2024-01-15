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

    Boolean deleteBySessionId(Long sessionId);

    QuickChatSession getByAccountId(String fromId, String toId);

    Boolean saveInfo(QuickChatSession chatSession);

    Boolean updateInfo(QuickChatSession chatSession1);

    List<QuickChatSession> getListByAccountIdList(List<String> fromIds, String toId);

    Boolean saveList(List<QuickChatSession> sessionPOList);
}
