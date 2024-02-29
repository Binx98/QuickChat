package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.enums.ChatTypeEnum;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.vo.ChatMsgVO;
import com.quick.service.QuickChatMsgService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatMsgStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import com.quick.strategy.chatmsg.ChatMsgStrategyFactory;
import com.quick.utils.ListUtil;
import com.quick.utils.RedissonLockUtil;
import com.quick.utils.RelationUtil;
import com.quick.utils.RequestContextUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 聊天信息 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatMsgServiceImpl extends ServiceImpl<QuickChatMsgMapper, QuickChatMsg> implements QuickChatMsgService {
    @Autowired
    private RedissonLockUtil lockUtil;
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 查询聊天记录
     */
    @Override
    public Map<String, List<ChatMsgVO>> getByRelationId(String relationId, Integer current, Integer size) {
        Page<QuickChatMsg> msgPage = msgStore.getByRelationId(relationId, current, size);
        List<ChatMsgVO> chatMsgVOList = ChatMsgAdapter.buildChatMsgVOList(msgPage.getRecords());
        return chatMsgVOList.stream()
                .sorted(Comparator.comparing(ChatMsgVO::getCreateTime))
                .collect(Collectors.groupingBy(ChatMsgVO::getRelationId));
    }

    /**
     * 查询双方聊天信息列表（首次登陆）
     */
    @Override
    public Map<String, List<ChatMsgVO>> getByAccountIds(List<String> accountIds) throws ExecutionException, InterruptedException {
        // 遍历生成 relation_id
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        List<String> relationIds = new ArrayList<>();
        for (String toAccountId : accountIds) {
            String relationId = RelationUtil.generate(loginAccountId, toAccountId);
            relationIds.add(relationId);
        }

        // 分组：10个/组，多线程异步查询聊天信息
        List<List<String>> relationIdList = ListUtil.fixedAssign(relationIds, 10);
        List<CompletableFuture<List<QuickChatMsg>>> futureList = new ArrayList<>();
        for (List<String> idList : relationIdList) {
            CompletableFuture<List<QuickChatMsg>> future = CompletableFuture.supplyAsync(
                    () -> msgStore.getByRelationIdList(idList), taskExecutor
            );
            futureList.add(future);
        }

        // 同步等待线程任务完毕，拿到聊天记录结果
        List<QuickChatMsg> msgResultList = new ArrayList<>();
        for (CompletableFuture<List<QuickChatMsg>> future : futureList) {
            List<QuickChatMsg> msgList = future.get();
            msgResultList.addAll(msgList);
        }

        // 转换成VO、按照 relation_id 分组
        List<ChatMsgVO> chatMsgVOList = ChatMsgAdapter.buildChatMsgVOList(msgResultList);
        Map<String, List<ChatMsgVO>> resultMap = chatMsgVOList.stream()
                .sorted(Comparator.comparing(ChatMsgVO::getCreateTime))
                .collect(Collectors.groupingBy(ChatMsgVO::getRelationId));

        // 没有 relation_id，空列表占位（首次发送消息需要占位）
        for (String relationId : relationIds) {
            if (!resultMap.containsKey(relationId)) {
                resultMap.put(relationId, new ArrayList<>());
            }
        }
        return resultMap;
    }

    @Override
    public void sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 保存聊天信息
        String fromId = msgDTO.getFromId();
        String toId = msgDTO.getToId();
        String content = msgDTO.getContent();
        Integer msgType = msgDTO.getMsgType();
        QuickChatMsg chatMsg = ChatMsgAdapter.buildChatMsgPO(fromId, toId, content, msgType);
        msgStore.saveMsg(chatMsg);

        // 双方会话创建
        String relationId = RelationUtil.generate(fromId, toId);
        QuickChatSession chatSession = lockUtil.executeWithLock(relationId, 15, TimeUnit.SECONDS,
                () -> this.handleSession(fromId, toId)
        );

        // 针对不同消息类型，策略模式处理
        AbstractChatMsgStrategy chatMsgHandler = ChatMsgStrategyFactory.getStrategyHandler(msgType);
        chatMsgHandler.sendChatMsg(chatMsg, chatSession);
    }

    @Transactional
    protected QuickChatSession handleSession(String fromId, String toId) {
        // 单聊：接受方没有会话，新增
        QuickChatSession toSession = sessionStore.getByAccountId(fromId, toId);
        if (ChatTypeEnum.SINGLE.getType().equals(toSession.getType())) {
            QuickChatSession sessionPO = sessionStore.getByAccountId(toId, fromId);
            if (ObjectUtils.isEmpty(sessionPO)) {
                sessionPO = ChatSessionAdapter.buildSessionPO(toId, fromId, toSession.getType());
                sessionStore.saveInfo(sessionPO);
            }
        }

        // 群聊：群成员没有会话，新增
        else {
            // 查询群成员列表
            String groupId = toId;
            List<QuickChatGroupMember> memberList = memberStore.getByGroupId(groupId);
            List<String> memberAccountIds = memberList.stream()
                    .map(QuickChatGroupMember::getAccountId)
                    .collect(Collectors.toList());

            // 查询群内成员会话列表
            List<QuickChatSession> memberSessionList = sessionStore.getListByAccountIdList(memberAccountIds, groupId);
            List<String> memberIds = memberSessionList.stream()
                    .map(QuickChatSession::getFromId)
                    .collect(Collectors.toList());

            // 过滤留下没有会话的用户列表、批量保存会话
            List<QuickChatSession> sessionPOList = new ArrayList<>();
            memberIds = memberIds.stream()
                    .filter(item -> !memberAccountIds.contains(item))
                    .collect(Collectors.toList());
            for (String accountId : memberIds) {
                sessionPOList.add(ChatSessionAdapter.buildSessionPO(accountId, groupId, toSession.getType()));
            }

            // 批量保存会话
            if (CollectionUtils.isNotEmpty(sessionPOList)) {
                sessionStore.saveList(sessionPOList);
            }
        }
        return toSession;
    }
}
