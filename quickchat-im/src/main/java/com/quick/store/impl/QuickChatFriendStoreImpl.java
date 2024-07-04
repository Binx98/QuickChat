package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatFriendMapper;
import com.quick.pojo.po.QuickChatFriendContact;
import com.quick.store.QuickChatFriendStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 聊天好友 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Service
public class QuickChatFriendStoreImpl extends ServiceImpl<QuickChatFriendMapper, QuickChatFriendContact> implements QuickChatFriendStore {
    @Override
    public List<QuickChatFriendContact> getListByFromId(String fromId) {
        return this.lambdaQuery()
                .eq(QuickChatFriendContact::getFromId, fromId)
                .list();
    }

    @Override
    public QuickChatFriendContact getByFromIdAndToId(String fromId, String toId) {
        return this.lambdaQuery()
                .eq(QuickChatFriendContact::getFromId, fromId)
                .eq(QuickChatFriendContact::getToId, toId)
                .one();
    }

    @Override
    public Boolean deleteByFromIdAndToId(String fromId, String toId) {
        return lambdaUpdate()
                .eq(QuickChatFriendContact::getFromId, fromId)
                .eq(QuickChatFriendContact::getToId, toId)
                .remove();
    }
}
