package com.quick.strategy.file;

import com.quick.enums.FileEnum;
import com.quick.pojo.dto.ChatMsgDTO;

import javax.annotation.PostConstruct;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:58
 * @Description: 文件策略抽象类
 * @Version: 1.0
 */
public abstract class AbstractFileStrategy {
    /**
     * 策略 Handler 注册到策略工厂
     */
    @PostConstruct
    private void initStrategyHandler() {
        FileStrategyFactory.register(this.getEnum().getType(), this);
    }

    /**
     * 获取当前实现类对应 Handler 枚举
     */
    protected abstract FileEnum getEnum();

    /**
     * 发送消息
     */
    public abstract void sendChatMsg(ChatMsgDTO msgDTO);
}
