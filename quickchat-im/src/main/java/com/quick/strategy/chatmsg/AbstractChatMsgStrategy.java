package com.quick.strategy.chatmsg;

import com.quick.adapter.ChatSessionAdapter;
import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ChatTypeEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatMsgStore;
import com.quick.store.QuickChatSessionStore;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:58
 * @Description: 聊天信息策略抽象类
 * @Version: 1.0
 */
@Component
public abstract class AbstractChatMsgStrategy {
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;
    @Autowired
    private QuickChatSessionStore sessionStore;

    /**
     * 策略 Handler 注册到策略工厂
     */
    @PostConstruct
    private void initStrategyHandler() {
        ChatMsgStrategyFactory.register(this.getEnum().getType(), this);
    }

    /**
     * 获取当前实现类对应 Handler 枚举
     */
    protected abstract ChatMsgEnum getEnum();

    /**
     * 发送消息
     */
    public abstract void sendChatMsg(ChatMsgDTO msgDTO) throws Throwable;

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 处理接收方会话
     */
    @Transactional
    protected Integer handleSession(String fromId, String toId) {
        // 单聊：接受方没有会话，新增
        QuickChatSession toSession = sessionStore.getByAccountId(fromId, toId);
        if (ChatTypeEnum.SINGLE.getType().equals(toSession.getType())) {
            if (ObjectUtils.isEmpty(toSession)) {
                QuickChatSession sessionPO = sessionStore.getByAccountId(toId, fromId);

                sessionPO = ChatSessionAdapter.buildSessionPO(toId, fromId);
                sessionStore.saveInfo(sessionPO);
            }
        }

        // 群聊：接收方是群内所有成员，没有会话，新增

        return null;
    }
}
