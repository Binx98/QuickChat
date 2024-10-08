package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatApplyMapper;
import com.quick.pojo.po.QuickChatApply;
import com.quick.store.QuickChatApplyStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统通知 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-03-12
 */
@Service
public class QuickChatApplyStoreImpl extends ServiceImpl<QuickChatApplyMapper, QuickChatApply> implements QuickChatApplyStore {

    @Override
    public QuickChatApply getByApplyId(Long applyId) {
        return this.getById(applyId);
    }

    @Override
    public List<QuickChatApply> getListByToId(String toId) {
        return this.lambdaQuery()
                .eq(QuickChatApply::getToId, toId)
                .orderByDesc(QuickChatApply::getCreateTime)
                .list();
    }

    @Override
    public Boolean saveApply(QuickChatApply apply) {
        return this.save(apply);
    }

    @Override
    public Boolean updateApplyStatus(Long applyId, Integer status) {
        return this.lambdaUpdate()
                .eq(QuickChatApply::getId, applyId)
                .set(QuickChatApply::getStatus, status)
                .update();
    }

    @Override
    public Boolean deleteByApplyId(Long applyId) {
        return this.removeById(applyId);
    }

    @Override
    public Boolean saveAll(List<QuickChatApply> applyList) {
        return this.saveBatch(applyList);
    }
}
