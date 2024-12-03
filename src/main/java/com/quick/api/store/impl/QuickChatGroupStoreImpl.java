package com.quick.api.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.api.mapper.QuickChatGroupMapper;
import com.quick.common.constant.RedisConstant;
import com.quick.common.pojo.po.QuickChatGroup;
import com.quick.api.store.QuickChatGroupStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群聊 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
@Service
public class QuickChatGroupStoreImpl extends ServiceImpl<QuickChatGroupMapper, QuickChatGroup> implements QuickChatGroupStore {
    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_GROUP, key = "'getByGroupId:' + #p0", unless = "#result == null")
    public QuickChatGroup getByGroupId(Long groupId) {
        return this.getById(groupId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_GROUP, key = "'getByGroupId:' + p0.id")
    })
    public Boolean updateInfo(QuickChatGroup chatGroup) {
        return this.updateById(chatGroup);
    }

    @Override
    public List<QuickChatGroup> getListByGroupIds(List<String> groupIds) {
        return this.lambdaQuery()
                .in(QuickChatGroup::getId, groupIds)
                .list();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_GROUP, key = "'getByGroupId:' + p0")
    })
    public Boolean dismissByGroupId(Long groupId) {
        return this.lambdaUpdate()
                .eq(QuickChatGroup::getId, groupId)
                .remove();
    }

    @Override
    public Boolean saveGroup(QuickChatGroup groupPO) {
        return this.save(groupPO);
    }
}
