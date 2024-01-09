package com.quick.strategy.chatmsg;

import com.quick.adapter.ChatSessionAdapter;
import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatSession;
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
     * 处理通信双方会话、未读数
     */
    @Transactional
    protected boolean handleSession(String fromId, String toId) {
        // 发送方
        QuickChatSession fromSession = sessionStore.getOneByAccountId(fromId, toId);
        if (ObjectUtils.isEmpty(fromSession)) {
            fromSession = ChatSessionAdapter.buildSessionPO(fromId, toId);
            sessionStore.saveInfo(fromSession);
        }

        // 接收方
        QuickChatSession toSession = sessionStore.getOneByAccountId(toId, fromId);
        if (ObjectUtils.isEmpty(toSession)) {
            toSession = ChatSessionAdapter.buildSessionPO(toId, fromId);
            sessionStore.saveInfo(toSession);
        }

        return true;
    }
}
