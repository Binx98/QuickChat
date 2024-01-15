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
     * 查询会话列表：限制50个会话以内
     */
    @Override
    public List<ChatSessionVO> getSessionList() {
        // 查询会话列表
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatSession> sessionList = sessionStore.getListByAccountId(loginAccountId);
        if (CollectionUtils.isEmpty(sessionList)) {
            return new ArrayList<>();
        }

        // 超过50个会话：超出部分已经已读，那就干掉
        sessionList = sessionList.stream().distinct().collect(Collectors.toList());
        if (sessionList.size() > 50) {
            List<QuickChatSession> over50List = sessionList.subList(49, sessionList.size());
            over50List = over50List.stream()
                    .filter(item -> item.getLastReadTime().isAfter(item.getUpdateTime()))
                    .collect(Collectors.toList());
            sessionList.removeAll(over50List);
        }

        // 按照单聊、群聊分组
        Map<Integer, List<QuickChatSession>> sessionListMap = sessionList.stream()
                .collect(Collectors.groupingBy(QuickChatSession::getType));

        // 单聊：会话列表
        List<QuickChatUser> users = new ArrayList<>();
        List<QuickChatSession> singleList = sessionListMap.get(ChatTypeEnum.SINGLE.getType());
        if (CollectionUtils.isNotEmpty(singleList)) {
            // 查询会话列表信息
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

        // 查询获取未读数量
        List<ChatSessionVO> chatSessionVOList = ChatSessionAdapter.buildSessionVOList(sessionList, users, groups);
        return null;
    }


    /**
     * 删除聊天会话
     */
    @Override
    public Boolean deleteSession(Long sessionId) {
        return sessionStore.deleteBySessionId(sessionId);
    }

    /**
     * 查询会话未读数量
     */
}
