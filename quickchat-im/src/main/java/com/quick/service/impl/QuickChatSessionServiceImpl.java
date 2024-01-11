package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.enums.ChatTypeEnum;
import com.quick.mapper.QuickChatSessionMapper;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatSessionVO;
import com.quick.service.QuickChatSessionService;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatSessionStore sessionStore;

    /**
     * 查询会话列表（TODO 考虑限制最大会话数：很久没发送消息的会话自动干掉）
     */
    @Override
    public List<ChatSessionVO> getSessionList() {
        // 查询会话列表
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatSession> sessionList = sessionStore.getListByAccountId(loginAccountId);
        if (CollectionUtils.isEmpty(sessionList)) {
            return new ArrayList<>();
        }

        // 先去重，后分组（单聊 or 群聊）
        Map<Integer, List<QuickChatSession>> sessionMap = sessionList.stream()
                .distinct()
                .collect(Collectors.groupingBy(QuickChatSession::getType));

        // 1.针对群聊
        List<QuickChatGroup> groupList = new ArrayList<>();
        List<QuickChatSession> groupSessionList = sessionMap.get(ChatTypeEnum.GROUP.getType());
        if (CollectionUtils.isNotEmpty(groupSessionList)) {
            List<String> groupIds = groupSessionList.stream()
                    .map(QuickChatSession::getToId)
                    .collect(Collectors.toList());
            groupList = groupStore.getListByGroupIds(groupIds);
        }

        // 2.针对单聊
        List<QuickChatUser> userList = new ArrayList<>();
        List<QuickChatSession> singleList = sessionMap.get(ChatTypeEnum.SINGLE.getType());
        if (CollectionUtils.isNotEmpty(singleList)) {
            List<String> accountIds = singleList.stream()
                    .map(QuickChatSession::getToId)
                    .collect(Collectors.toList());
            userList = userStore.getListByAccountIds(accountIds);
        }

        return ChatSessionAdapter.buildSessionVOList(sessionList, userList, groupList);
    }

    /**
     * 删除聊天会话
     */
    @Override
    public Boolean deleteSession(Long sessionId) {
        return sessionStore.deleteBySessionId(sessionId);
    }
}
