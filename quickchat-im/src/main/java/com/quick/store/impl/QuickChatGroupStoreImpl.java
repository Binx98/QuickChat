package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.store.QuickChatGroupStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    /**
     * 根据 group_id 查询群信息
     */
    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_GROUP, key = "#p0", unless = "#result == null")
    public QuickChatGroup getByGroupId(Long groupId) {
        return this.getById(groupId);
    }

    /**
     * 根据 group_id 修改群聊信息
     */
    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_GROUP, key = "#p0.groupId")
    public Boolean updateInfo(QuickChatGroup chatGroup) {
        return this.updateById(chatGroup);
    }

    /**
     * 根据 group_id 列表查询群信息
     */
    @Override
    public List<QuickChatGroup> getListByGroupIds(List<String> groupIds) {
        return this.lambdaQuery()
                .in(QuickChatGroup::getGroupId, groupIds)
                .list();
    }
}
