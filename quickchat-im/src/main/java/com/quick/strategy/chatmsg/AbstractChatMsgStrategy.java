package com.quick.strategy.chatmsg;

import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:58
 * @Description: 聊天信息策略抽象类
 * @Version: 1.0
 */
@Component
public abstract class AbstractChatMsgStrategy {
    /**
     * 策略 Handler 注册到策略工厂
     */
    @PostConstruct
    private void initStrategyHandler() {
        ChatMsgStrategyFactory.register(this.getEnum().getType(), this);
    }

    /**
     * 获取当前实现类对应 Handler 枚举
     *
     * @return 聊天信息策略枚举
     */
    protected abstract ChatMsgEnum getEnum();

    /**
     * 发送聊天信息
     *
     * @param chatMsg     聊天信息入参
     * @param chatSession 会话信息入参
     * @throws Throwable 异常
     */
    public abstract void sendChatMsg(QuickChatMsg chatMsg, QuickChatSession chatSession) throws Throwable;
}
