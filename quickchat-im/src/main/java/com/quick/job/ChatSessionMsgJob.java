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
     * 迁移超过 30日 聊天记录到 Doris
     */
    @XxlJob("MoveChatMsgToDorisJob")
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
        record.setStatus(YesNoEnum.NO.getCode());
        archiveRecordStore.saveArchiveRecord(record);
        this.handleMoveProcess(msgList, record, "MoveChatMsgToDorisJob");
        return ReturnT.SUCCESS;
    }

    /**
     * 校验数据迁移 Doris 状态
     */
    @XxlJob("CheckMoveDorisArchiveJob")
    public ReturnT checkMoveDorisArchiveJob() {
        List<QuickChatArchiveRecord> archiveRecordList = archiveRecordStore.getListByStatus(YesNoEnum.NO.getCode());
        if (CollectionUtils.isEmpty(archiveRecordList)) {
            return ReturnT.SUCCESS;
        }
        // 超过1小时数据都没迁移完毕，那就是迁移失败了
        QuickChatArchiveRecord record = archiveRecordList.get(0);
        if (LocalDateTime.now().minusHours(1).isBefore(record.getCreateTime())) {
            return ReturnT.SUCCESS;
        }
        List<QuickChatMsg> msgList = msgStore.getMsgByTime(record.getStartTime(), record.getEndTime());
        this.handleMoveProcess(msgList, record, "CheckMoveDorisArchiveJob");
        return ReturnT.SUCCESS;
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleMoveProcess(List<QuickChatMsg> msgList, QuickChatArchiveRecord record, String jobName) {
        log.info("--------------[" + jobName + "] job start, count: {}--------------", record.getCount());
        msgDorisStore.saveBatchMsg(msgList);
        List<Long> msgIds = msgList.stream()
                .map(QuickChatMsg::getId)
                .collect(Collectors.toList());
        msgStore.deleteNoLogicMsgListByIds(msgIds);
        record.setStatus(YesNoEnum.YES.getCode());
        archiveRecordStore.updateArchiveRecord(record);
        log.info("--------------[" + jobName + "] job finish!--------------");
    }
}
