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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    /**
     * 查询会话列表：限制50个会话以内
     */
    @Override
    public List<ChatSessionVO> getSessionList() throws ExecutionException, InterruptedException {
        // 查询会话列表
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatSession> sessionList = sessionStore.getListByAccountId(loginAccountId);
        if (CollectionUtils.isEmpty(sessionList)) {
            return new ArrayList<>();
        }

        // 超过50个会话：超出部分已经已读，那就干掉
        if (sessionList.size() > 50) {
            List<QuickChatSession> over50List = sessionList.subList(49, sessionList.size());
            over50List = over50List.stream()
                    .filter(item -> item.getLastReadTime().isAfter(item.getUpdateTime()))
                    .collect(Collectors.toList());
            sessionList.removeAll(over50List);
        }

        // 按照单聊、群聊分组
        Map<Integer, List<QuickChatSession>> sessionListMap = sessionList.stream()
                .distinct()
                .collect(Collectors.groupingBy(QuickChatSession::getType));

        // 单聊
        CompletableFuture<List<QuickChatUser>> userFuture = null;
        List<QuickChatSession> singleList = sessionListMap.get(ChatTypeEnum.SINGLE.getType());
        if (CollectionUtils.isNotEmpty(singleList)) {
            userFuture = CompletableFuture.supplyAsync(() -> {
                List<String> accountIds = singleList.stream()
                        .map(QuickChatSession::getToId)
                        .collect(Collectors.toList());
                return userStore.getListByAccountIds(accountIds);
            }, poolExecutor);
        }

        // 群聊
        CompletableFuture<List<QuickChatGroup>> groupFuture = null;
        List<QuickChatSession> groupList = sessionListMap.get(ChatTypeEnum.GROUP.getType());
        if (CollectionUtils.isNotEmpty(groupList)) {
            groupFuture = CompletableFuture.supplyAsync(() -> {
                List<String> groupIds = groupList.stream()
                        .map(QuickChatSession::getToId)
                        .collect(Collectors.toList());
                return groupStore.getListByGroupIds(groupIds);
            }, poolExecutor);
        }

        // 等待线程执行完毕，封装结果
        CompletableFuture.allOf(userFuture, groupFuture);
        return ChatSessionAdapter.buildSessionVOList(sessionList, userFuture.get(), groupFuture.get());
    }

    /**
     * 删除聊天会话
     */
    @Override
    public Boolean deleteSession(Long sessionId) {
        return sessionStore.deleteBySessionId(sessionId);
    }
}
