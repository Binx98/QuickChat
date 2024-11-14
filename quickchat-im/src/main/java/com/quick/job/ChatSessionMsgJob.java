package com.quick.job;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.quick.enums.YesNoEnum;
import com.quick.pojo.po.QuickChatArchiveRecord;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.doris.QuickChatMsgDorisStore;
import com.quick.store.mysql.QuickChatArchiveRecordStore;
import com.quick.store.mysql.QuickChatMsgStore;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private QuickChatMsgDorisStore msgDorisStore;
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private QuickChatArchiveRecordStore archiveRecordStore;

    /**
     * 保留 30 日聊天记录（凌晨 2：00 迁移到 Doris）
     */
    @XxlJob("MoveChatMsgToDorisJob")
    @Transactional(rollbackFor = Exception.class)
    public ReturnT moveChatMsgToDorisJob() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.minusDays(30);
        List<QuickChatMsg> msgList = msgStore.getMsgByTime(startTime, endTime);
        if (CollectionUtils.isEmpty(msgList)) {
            return ReturnT.SUCCESS;
        }

        QuickChatArchiveRecord record = new QuickChatArchiveRecord();
        record.setBeginId(msgList.get(0).getId());
        record.setEndId(msgList.get(msgList.size() - 1).getId());
        record.setStartTime(startTime);
        record.setEndTime(endTime);
        record.setCount(Long.valueOf(msgList.size()));
        record.setStatus(YesNoEnum.YES.getCode());
        archiveRecordStore.saveArchiveRecord(record);

        Boolean moveDorisFlag = msgDorisStore.saveBatchMsg(msgList);
        if (!moveDorisFlag) {
            return ReturnT.FAIL;
        }

        List<Long> msgIds = msgList.stream()
                .map(QuickChatMsg::getId)
                .collect(Collectors.toList());
        msgStore.deleteNoLogicMsgListByIds(msgIds);

        record.setStatus(YesNoEnum.NO.getCode());
        archiveRecordStore.updateArchiveRecord(record);
        return ReturnT.SUCCESS;
    }
}
