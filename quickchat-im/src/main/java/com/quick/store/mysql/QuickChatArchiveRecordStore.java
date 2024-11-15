package com.quick.store.mysql;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatArchiveRecord;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-11-13
 */
public interface QuickChatArchiveRecordStore extends IService<QuickChatArchiveRecord> {
    /**
     * 保存数据迁移记录
     *
     * @param record 迁移记录
     * @return 执行结果
     */
    Boolean saveArchiveRecord(QuickChatArchiveRecord record);

    /**
     * 修改迁移记录
     *
     * @param record 迁移记录
     * @return 执行结果
     */
    Boolean updateArchiveRecord(QuickChatArchiveRecord record);

    /**
     * 根据状态查询迁移记录
     *
     * @param status 状态
     * @return 迁移记录
     */
    List<QuickChatArchiveRecord> getListByStatus(Integer status);
}
