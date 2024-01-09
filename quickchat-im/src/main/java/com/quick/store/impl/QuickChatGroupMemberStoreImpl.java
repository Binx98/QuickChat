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
    /**
     * 查询群成员列表
     */
    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_GROUP_MEMBER, key = "#p0", unless = "#result.isEmpty()")
    public List<QuickChatGroupMember> getByGroupId(String groupId) {
        return this.lambdaQuery()
                .eq(QuickChatGroupMember::getGroupId, groupId)
                .list();
    }

    /**
     * 加入群聊
     */
    @Override
    public Boolean enterGroup(QuickChatGroupMember memberPO) {
        return this.save(memberPO);
    }
}
