package com.quick.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public List<ChatSessionVO> getSessionList() {
        // 查询会话列表
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatSession> sessionList = sessionStore.getListByAccountId(loginAccountId);
        if (CollectionUtils.isEmpty(sessionList)) {
            return new ArrayList<>();
        }

        // 超过50个会话：超出部分已读直接干掉
        sessionList = sessionList.stream().distinct().collect(Collectors.toList());
        if (sessionList.size() > 50) {
            List<QuickChatSession> subSessionList = sessionList.subList(49, sessionList.size());
            List<QuickChatSession> over50List = subSessionList.stream()
                    .filter(item -> item.getLastReadTime().isAfter(item.getUpdateTime()))
                    .collect(Collectors.toList());
            sessionList.removeAll(over50List);
        }

        // 单聊：会话列表
        Map<Integer, List<QuickChatSession>> sessionListMap = sessionList.stream()
                .collect(Collectors.groupingBy(QuickChatSession::getType));
        List<QuickChatUser> users = new ArrayList<>();
        List<QuickChatSession> singleList = sessionListMap.get(ChatTypeEnum.SINGLE.getType());
        if (CollectionUtils.isNotEmpty(singleList)) {
            List<String> accountIds = singleList.stream()
                    .map(QuickChatSession::getToId)
                    .collect(Collectors.toList());
            users = userStore.getListByAccountIds(accountIds);
        }

        // 群聊：会话列表
        List<QuickChatGroup> groups = new ArrayList<>();
        List<QuickChatSession> groupList = sessionListMap.get(ChatTypeEnum.GROUP.getType());
        if (CollectionUtils.isNotEmpty(groupList)) {
            List<String> groupIds = groupList.stream()
                    .map(QuickChatSession::getToId)
                    .collect(Collectors.toList());
            groups = groupStore.getListByGroupIds(groupIds);
        }
        List<ChatSessionVO> sessionVOList = ChatSessionAdapter.buildSessionVOList(sessionList, users, groups);

        // 查询会话未读数量
        Map<String, Integer> unreadCountMap = this.getUnreadCountMap(sessionVOList);
        for (ChatSessionVO sessionVO : sessionVOList) {
            String relationId = sessionVO.getRelationId();
            if (unreadCountMap.containsKey(relationId)) {
                sessionVO.setUnreadCount(unreadCountMap.get(relationId));
            }
        }
        return sessionVOList;
    }

    @Override
    public Boolean deleteSession(Long sessionId) {
        return sessionStore.deleteBySessionId(sessionId);
    }

    @Override
    public Boolean updateLastReadTime(Long sessionId) {
        QuickChatSession sessionPO = ChatSessionAdapter.buildSessionPO(sessionId, LocalDateTime.now());
        return sessionStore.updateInfo(sessionPO);
    }

    @Override
    public Map<String, Integer> getUnreadCountMap(List<ChatSessionVO> sessionList) {
        Map<String, Integer> resultMap = new HashMap<>();
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        for (ChatSessionVO session : sessionList) {
            String relationId = session.getRelationId();
            LocalDateTime lastReadTime = session.getLastReadTime();
            Integer unreadCount = sessionStore.getUnreadCount(loginAccountId, relationId, lastReadTime);
            resultMap.put(relationId, unreadCount);
        }
        List<String> relationIds = sessionList.stream()
                .map(ChatSessionVO::getRelationId)
                .collect(Collectors.toList());
        for (String relationId : relationIds) {
            if (!resultMap.containsKey(relationId)) {
                resultMap.put(relationId, null);
            }
        }
        return resultMap;
    }

    @Override
    public ChatSessionVO getSessionInfo(String fromId, String toId) {
        QuickChatSession sessionPO = sessionStore.getByAccountId(fromId, toId);
        if (ObjectUtils.isEmpty(sessionPO)) {
            return null;
        }
        ChatSessionVO sessionVO = null;
        if (ChatTypeEnum.SINGLE.getType().equals(sessionPO.getType())) {
            QuickChatUser userPO = userStore.getByAccountId(sessionPO.getToId());
            sessionVO = ChatSessionAdapter.buildUserSessionPO(userPO, sessionPO);
        } else {
            QuickChatGroup groupPO = groupStore.getByGroupId(sessionPO.getToId());
            sessionVO = ChatSessionAdapter.buildGroupSessionPO(groupPO, sessionPO);
        }
        Map<String, Integer> unreadCountMap = this.getUnreadCountMap(ListUtil.of(sessionVO));
        sessionVO.setUnreadCount(unreadCountMap.get(sessionVO.getRelationId()));
        return sessionVO;
    }
}
