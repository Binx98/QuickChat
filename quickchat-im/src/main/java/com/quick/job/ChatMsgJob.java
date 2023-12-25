package com.quick.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2023/12/25 20:50
 * @Version 1.0
 * @Description: ChatMsgJob
 */
@Component
public class ChatMsgJob {
    /**
     * TODO 超过30天聊天记录删除！ Terry
     */
    @XxlJob("RemoveOver30DayChatMsg")
    public ReturnT removeOver30DayChatMsg() {
        return ReturnT.SUCCESS;
    }
}
