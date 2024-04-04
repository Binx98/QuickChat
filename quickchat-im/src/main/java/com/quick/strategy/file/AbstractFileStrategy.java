package com.quick.strategy.file;

import com.quick.enums.BucketEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-04-04  13:58
 * @Description: 文件上传策略抽象类
 * @Version: 1.0
 */
@Component
public abstract class AbstractFileStrategy {
    @PostConstruct
    private void initStrategyHandler() {
        FileStrategyFactory.register(this.getEnum().getCode(), this);
    }

    /**
     * 获取当前策略枚举
     *
     * @return Bucket桶枚举
     */
    protected abstract BucketEnum getEnum();

    /**
     * 发送聊天消息
     *
     * @param msgDTO 消息参数
     * @return 消息实体PO
     * @throws Throwable 顶级异常
     */
    public abstract QuickChatMsg sendMsg(ChatMsgDTO msgDTO) throws Throwable;
}
