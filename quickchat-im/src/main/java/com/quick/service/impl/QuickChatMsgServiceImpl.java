package com.quick.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.constant.KafkaConstant;
import com.quick.enums.SessionTypeEnum;
import com.quick.kafka.KafkaProducer;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatMsgVO;
import com.quick.service.QuickChatMsgService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatMsgStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.store.QuickChatUserStore;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import com.quick.strategy.msg.ChatMsgStrategyFactory;
import com.quick.utils.RedissonLockUtil;
import com.quick.utils.RelationUtil;
import com.quick.utils.RequestContextUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private KafkaProducer kafkaProducer;
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    public Map<String, List<ChatMsgVO>> getByRelationId(String relationId, Integer current, Integer size) {
        Page<QuickChatMsg> msgPage = msgStore.getByRelationId(relationId, current, size);
        if (CollectionUtils.isEmpty(msgPage.getRecords())) {
            return new HashMap<>();
        }
        List<ChatMsgVO> chatMsgVOList = ChatMsgAdapter.buildChatMsgVOList(msgPage.getRecords());
        Map<String, List<ChatMsgVO>> resultMap = chatMsgVOList.stream()
                .sorted(Comparator.comparing(ChatMsgVO::getCreateTime))
                .collect(Collectors.groupingBy(ChatMsgVO::getRelationId));
        return resultMap;
    }

    @Override
    public Map<String, List<ChatMsgVO>> getByAccountIds(List<String> toIds) {
        // 区分用户和群聊
        List<String> relationIds = new ArrayList<>();
        List<QuickChatUser> userList = userStore.getListByAccountIds(toIds);
        List<String> accountIds = userList.stream()
                .map(QuickChatUser::getAccountId)
                .collect(Collectors.toList());
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        for (String toId : toIds) {
            if (accountIds.contains(toId)) {
                relationIds.add(RelationUtil.generate(loginAccountId, toId));
            } else {
                relationIds.add(toId);
            }
        }

        // 查询聊天信息
        List<QuickChatMsg> msgList = msgStore.getByRelationIdList(relationIds);
        List<ChatMsgVO> chatMsgVOList = ChatMsgAdapter.buildChatMsgVOList(msgList);
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
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 处理双方会话框
        String relationId = RelationUtil.generate(msgDTO.getFromId(), msgDTO.getToId());
        QuickChatSession chatSession = lockUtil.executeWithLock(relationId, 15, TimeUnit.SECONDS,
                () -> this.handleSession(msgDTO.getFromId(), msgDTO.getToId())
        );

        // 发送消息
        AbstractChatMsgStrategy chatMsgHandler = ChatMsgStrategyFactory.getStrategyHandler(msgDTO.getMsgType());
        msgDTO.setSessionType(chatSession.getType());
        QuickChatMsg chatMsg = chatMsgHandler.sendMsg(msgDTO);

        // 通过 Channel 推送给客户端（单聊、群聊）
        if (SessionTypeEnum.SINGLE.getCode().equals(chatSession.getType())) {
            kafkaProducer.send(KafkaConstant.SEND_CHAT_SINGLE_MSG, JSONUtil.toJsonStr(chatMsg));
        } else if (SessionTypeEnum.GROUP.getCode().equals(chatSession.getType())) {
            kafkaProducer.send(KafkaConstant.SEND_CHAT_GROUP_MSG, JSONUtil.toJsonStr(chatMsg));
        }
    }

    @Override
    public void entering(String fromId, String toId) {
        Map<String, String> param = new HashMap<>();
        param.put("fromId", fromId);
        param.put("toId", toId);
        kafkaProducer.send(KafkaConstant.SEND_CHAT_ENTERING, JSONUtil.toJsonStr(param));
    }

    /**
     * 处理双方会话
     *
     * @param fromId 发送方账户id
     * @param toId   接收方账户id
     * @return 接收方会话信息
     */
    private QuickChatSession handleSession(String fromId, String toId) {
        // 单聊：接收方没有会话，新增
        QuickChatSession toSession = sessionStore.getByAccountId(fromId, toId);
        if (SessionTypeEnum.SINGLE.getCode().equals(toSession.getType())) {
            QuickChatSession sessionPO = sessionStore.getByAccountId(toId, fromId);
            if (ObjectUtils.isEmpty(sessionPO)) {
                sessionPO = ChatSessionAdapter.buildSessionPO(toId, fromId, toSession.getType());
                sessionStore.saveInfo(sessionPO);
            }
        }

        // 群聊：群成员没有会话，新增
        else {
            // 查询群成员列表
            List<QuickChatGroupMember> memberList = memberStore.getListByGroupId(Long.valueOf(toId));
            List<String> memberAccountIds = memberList.stream()
                    .map(QuickChatGroupMember::getAccountId)
                    .collect(Collectors.toList());

            // 查询群内成员会话列表
            List<QuickChatSession> memberSessionList = sessionStore.getListByAccountIdList(memberAccountIds, toId);
            List<String> memberIds = memberSessionList.stream()
                    .map(QuickChatSession::getFromId)
                    .collect(Collectors.toList());

            // 过滤留下没有会话的用户列表、批量保存会话
            List<QuickChatSession> sessionPOList = new ArrayList<>();
            memberIds = memberIds.stream()
                    .filter(item -> !memberAccountIds.contains(item))
                    .collect(Collectors.toList());
            for (String accountId : memberIds) {
                sessionPOList.add(ChatSessionAdapter.buildSessionPO(accountId, toId, toSession.getType()));
            }

            // 批量保存会话
            if (CollectionUtils.isNotEmpty(sessionPOList)) {
                sessionStore.saveList(sessionPOList);
            }
        }
        return toSession;
    }
}
