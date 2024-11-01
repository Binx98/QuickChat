package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatMsgStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 聊天信息 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatMsgStoreImpl extends ServiceImpl<QuickChatMsgMapper, QuickChatMsg> implements QuickChatMsgStore {
    @Override
    public Boolean saveMsg(QuickChatMsg chatMsg) {
        return this.save(chatMsg);
    }

    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_MSG, key = "'getByRelationId:' + #p0 + #p1 + #p2", unless = "#result.records.isEmpty()")
    public Page<QuickChatMsg> getByRelationId(Long relationId, Integer current, Integer size) {
        return this.lambdaQuery()
                .eq(QuickChatMsg::getRelationId, relationId)
                .orderByDesc(QuickChatMsg::getCreateTime)
                .page(new Page<>(current, size));
    }

    @Override
    public List<QuickChatMsg> getByRelationIds(List<Long> relationIds, Integer size) {
        return this.lambdaQuery()
                .in(QuickChatMsg::getRelationId, relationIds)
                .orderByDesc(QuickChatMsg::getCreateTime)
                .last(" LIMIT " + size)
                .list();
    }

    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_MSG, key = "'getByMsgId:' + #p0", unless = "#result == null")
    public QuickChatMsg getByMsgId(Long msgId) {
        return this.lambdaQuery()
                .eq(QuickChatMsg::getId, msgId)
                .one();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_APPLY, key = "'getByMsgId:' + #p0.id"),
            @CacheEvict(value = RedisConstant.QUICK_CHAT_APPLY, key = "'getByRelationId:' + #p0.relationId", allEntries = true)
    })
    public Boolean updateByMsgId(QuickChatMsg chatMsg) {
        return this.updateById(chatMsg);
    }


    @Override
    public Integer getUnreadCount(String loginAccountId, Long relationId, LocalDateTime lastReadTime) {
        return this.lambdaQuery()
                .eq(QuickChatMsg::getRelationId, relationId)
                .ne(QuickChatMsg::getFromId, loginAccountId)
                .gt(QuickChatMsg::getCreateTime, lastReadTime)
                .groupBy(QuickChatMsg::getRelationId)
                .count();
    }
}
