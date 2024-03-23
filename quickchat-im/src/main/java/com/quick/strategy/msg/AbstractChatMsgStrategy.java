package com.quick.strategy.msg;

import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
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
    @PostConstruct
    private void initStrategyHandler() {
        ChatMsgStrategyFactory.register(this.getEnum().getCode(), this);
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
    public abstract QuickChatMsg sendChatMsg(ChatMsgDTO msgDTO) throws Throwable;
}
