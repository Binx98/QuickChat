package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatApplyMapper;
import com.quick.pojo.po.QuickChatApply;
import com.quick.store.QuickChatApplyStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @Cacheable(value = RedisConstant.QUICK_CHAT_APPLY, key = "'getByApplyId:' + #p0", unless = "#result == null")
    public QuickChatApply getByApplyId(Long applyId) {
        return this.getById(applyId);
    }

    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_APPLY, key = "'getListByToId:' + #p0", unless = "#result.isEmpty()")
    public List<QuickChatApply> getListByToId(String toId) {
        return this.lambdaQuery()
                .eq(QuickChatApply::getToId, toId)
                .orderByDesc(QuickChatApply::getCreateTime)
                .list();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_APPLY, key = "'getListByToId:' + #p0.toId")
    })
    public Boolean saveApply(QuickChatApply apply) {
        return this.save(apply);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_APPLY, key = "'getByApplyId:' + #p0"),
            @CacheEvict(value = RedisConstant.QUICK_CHAT_APPLY, key = "'getListByToId:' + #p1")
    })
    public Boolean updateApplyStatus(Long applyId, String toId, Integer status) {
        return this.lambdaUpdate()
                .eq(QuickChatApply::getId, applyId)
                .eq(QuickChatApply::getToId, toId)
                .set(QuickChatApply::getStatus, status)
                .update();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_APPLY, key = "'getByApplyId:' + #p0"),
            @CacheEvict(value = RedisConstant.QUICK_CHAT_APPLY, key = "'getListByToId:' + #p1")
    })
    public Boolean deleteByApplyId(Long applyId, String toId) {
        return this.removeById(applyId);
    }

    @Override
    public Boolean saveAll(List<QuickChatApply> applyList) {
        return this.saveBatch(applyList);
    }
}
