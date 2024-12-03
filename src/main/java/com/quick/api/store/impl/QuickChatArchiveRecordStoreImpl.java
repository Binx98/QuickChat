package com.quick.api.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.api.mapper.QuickChatArchiveRecordMapper;
import com.quick.api.store.QuickChatArchiveRecordStore;
import com.quick.common.pojo.po.QuickChatArchiveRecord;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-11-13
 */
@Service
public class QuickChatArchiveRecordStoreImpl extends ServiceImpl<QuickChatArchiveRecordMapper, QuickChatArchiveRecord> implements QuickChatArchiveRecordStore {
    @Override
    public Boolean saveArchiveRecord(QuickChatArchiveRecord record) {
        return this.save(record);
    }

    @Override
    public Boolean updateArchiveRecord(QuickChatArchiveRecord record) {
        return this.updateById(record);
    }

    @Override
    public List<QuickChatArchiveRecord> getListByStatus(Integer status) {
        return this.lambdaQuery()
                .eq(QuickChatArchiveRecord::getStatus, status)
                .list();
    }
}
