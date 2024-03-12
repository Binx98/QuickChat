package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatFriendMapper;
import com.quick.pojo.po.QuickChatFriend;
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
public class QuickChatFriendStoreImpl extends ServiceImpl<QuickChatFriendMapper, QuickChatFriend> implements QuickChatFriendStore {
    @Override
    public List<QuickChatFriend> getListByFromId(String fromId) {
        return this.lambdaQuery()
                .eq(QuickChatFriend::getFromId, fromId)
                .list();
    }

    @Override
    public QuickChatFriend getByFromIdAndToId(String fromId, String toId) {
        return this.lambdaQuery()
                .eq(QuickChatFriend::getFromId, fromId)
                .eq(QuickChatFriend::getToId, toId)
                .one();
    }
}
