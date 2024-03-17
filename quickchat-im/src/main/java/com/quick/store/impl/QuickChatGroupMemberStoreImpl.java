package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatGroupMemberMapper;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.store.QuickChatGroupMemberStore;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群成员 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
@Service
public class QuickChatGroupMemberStoreImpl extends ServiceImpl<QuickChatGroupMemberMapper, QuickChatGroupMember> implements QuickChatGroupMemberStore {
    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_GROUP_MEMBER, key = "#p0", unless = "#result.isEmpty()")
    public List<QuickChatGroupMember> getByGroupId(String groupId) {
        return this.lambdaQuery()
                .eq(QuickChatGroupMember::getGroupId, groupId)
                .list();
    }

    @Override
    public Boolean enterGroup(QuickChatGroupMember memberPO) {
        return this.save(memberPO);
    }

    @Override
    public Boolean deleteByGroupIdAndAccountId(String groupId, String accountId) {
        return this.lambdaUpdate()
                .eq(QuickChatGroupMember::getGroupId, groupId)
                .eq(QuickChatGroupMember::getAccountId, accountId)
                .remove();
    }

    @Override
    public Boolean deleteByGroupId(String groupId) {
        return this.lambdaUpdate()
                .eq(QuickChatGroupMember::getGroupId, groupId)
                .remove();
    }
}
