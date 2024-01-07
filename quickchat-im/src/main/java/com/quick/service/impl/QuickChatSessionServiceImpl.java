package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.mapper.QuickChatSessionMapper;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatSessionVO;
import com.quick.service.QuickChatMsgService;
import com.quick.service.QuickChatSessionService;
import com.quick.store.QuickChatSessionStore;
import com.quick.store.QuickUserStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 聊天会话（针对单聊） 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatSessionServiceImpl extends ServiceImpl<QuickChatSessionMapper, QuickChatSession> implements QuickChatSessionService {
    @Autowired
    private QuickUserStore userStore;
    @Autowired
    private QuickChatMsgService msgService;
    @Autowired
    private QuickChatSessionStore sessionStore;

    /**
     * 查询会话列表
     */
    @Override
    public List<ChatSessionVO> getSessionList() {
        // 查询会话列表
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatSession> sessionList = sessionStore.getListByAccountId(loginAccountId);
        List<String> toAccountIds = sessionList.stream()
                .map(QuickChatSession::getToId)
                .collect(Collectors.toList());

        // 查询会话用户信息
        List<QuickChatUser> userList = userStore.getListByAccountIds(toAccountIds);
        return ChatSessionAdapter.buildSessionVOList(sessionList, userList);
    }

    /**
     * 删除聊天会话
     */
    @Override
    public Boolean deleteSession(Long sessionId) {
        return sessionStore.deleteBySessionId(sessionId);
    }
}
