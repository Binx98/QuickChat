package com.quick.job;

import com.quick.service.QuickChatMsgService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2024/4/4 13:44
 * @Version 1.0
 * @Description: 会话、聊天信息定时 Job
 */
@Slf4j
@Component
public class ChatSessionMsgJob {
    @Autowired
    private QuickChatMsgService msgService;

    /**
     * TODO 保留30日聊天记录（凌晨 2：00 迁移到 Doris）
     */
    @XxlJob("MoveHistoryMsgToDorisJob")
    public ReturnT moveHistoryMsgToDorisJob() {
        return ReturnT.SUCCESS;
    }
}
