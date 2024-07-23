package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.store.QuickChatGroupStore;
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
    public QuickChatGroup getByGroupId(Long groupId) {
        return this.getById(groupId);
    }

    @Override
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
    public List<QuickChatGroup> getListByAccountId(String accountId) {
        return this.lambdaQuery()
                .eq(QuickChatGroup::getAccountId, accountId)
                .list();
    }

    @Override
    public Boolean dismissByGroupId(String groupId) {
        return this.lambdaUpdate()
                .eq(QuickChatGroup::getId, groupId)
                .remove();
    }

    @Override
    public Boolean saveGroup(QuickChatGroup groupPO) {
        return this.save(groupPO);
    }
}
