package com.quick.strategy.msg;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ChatTypeEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatSessionStore;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:58
 * @Description: 聊天信息策略抽象类
 * @Version: 1.0
 */
@Component
public abstract class AbstractChatMsgStrategy {
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @PostConstruct
    private void initStrategyHandler() {
        ChatMsgStrategyFactory.register(this.getEnum().getType(), this);
    }

    /**
     * 获取当前策略对应聊天消息枚举
     *
     * @return 聊天消息枚举
     */
    protected abstract ChatMsgEnum getEnum();

    /**
     * 发送聊天消息
     *
     * @param msgDTO 消息体入参
     * @throws Throwable 异常
     */
    public abstract void sendChatMsg(ChatMsgDTO msgDTO) throws Throwable;

    /**
     * 处理双方会话
     *
     * @param fromId 发送方账户id
     * @param toId   接收方账户id
     * @return 接收方会话信息
     */
    protected QuickChatSession handleSession(String fromId, String toId) {
        // 单聊：接收方没有会话，新增
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
            List<QuickChatGroupMember> memberList = memberStore.getByGroupId(toId);
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
