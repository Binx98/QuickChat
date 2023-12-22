package com.quick.strategy.chatmsg;

import com.quick.adapter.ChatSessionAdapter;
import com.quick.constant.RedisConstant;
import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatSessionStore;
import com.quick.utils.AESUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

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
     * 生成会话锁Key
     */
    protected String generateSessionLockKey(String sendAccountId, String receiveAccountId) throws Exception {
        String[] accountIdArr = {sendAccountId, receiveAccountId};
        Arrays.sort(accountIdArr);
        String sessionKey = accountIdArr[0] + accountIdArr[1];
        return RedisConstant.UNREAD_LOCK_KEY + AESUtil.encrypt(sessionKey);
    }

    /**
     * 处理通信双方会话、未读数
     */
    protected boolean handleSession(String sendAccountId, String receiveAccountId) {
        // 发送方
        QuickChatSession chatSession1 = sessionStore.getOneByAccountId(sendAccountId, receiveAccountId);
        if (ObjectUtils.isEmpty(chatSession1)) {
            chatSession1 = ChatSessionAdapter.buildSessionPO(sendAccountId, receiveAccountId, 1);
            sessionStore.saveInfo(chatSession1);
        } else {
            chatSession1.setUnreadCount(chatSession1.getUnreadCount() + 1);
            sessionStore.updateInfo(chatSession1);
        }

        // 接收方
        QuickChatSession chatSession2 = sessionStore.getOneByAccountId(receiveAccountId, sendAccountId);
        if (ObjectUtils.isEmpty(chatSession2)) {
            chatSession2 = ChatSessionAdapter.buildSessionPO(receiveAccountId, sendAccountId, 1);
            return sessionStore.saveInfo(chatSession2);
        } else {
            chatSession2.setUnreadCount(chatSession2.getUnreadCount() + 1);
            return sessionStore.updateInfo(chatSession2);
        }
    }
}
