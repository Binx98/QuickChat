package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.mapper.QuickChatSessionMapper;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatSessionVO;
import com.quick.service.QuickChatSessionService;
import com.quick.store.QuickChatSessionStore;
import com.quick.store.QuickUserStore;
import com.quick.utils.RequestHolderUtil;
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
    private QuickChatSessionStore sessionStore;

    /**
     * 查询会话列表
     */
    @Override
    public List<ChatSessionVO> getSessionList() {
        // 查询会话列表
        String accountId = (String) RequestHolderUtil.get().get("account_id");
        List<QuickChatSession> sessionList = sessionStore.getListByAccountId(accountId);
        List<String> receiveIds = sessionList.stream()
                .map(QuickChatSession::getFromId)
                .collect(Collectors.toList());

        // 查询会话用户信息
        List<QuickChatUser> userList = userStore.getListByAccountIds(receiveIds);

        // 封装结果集
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
