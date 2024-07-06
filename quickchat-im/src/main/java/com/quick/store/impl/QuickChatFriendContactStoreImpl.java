package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatFriendContactMapper;
import com.quick.pojo.po.QuickChatContact;
import com.quick.store.QuickChatFriendContactStore;
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
public class QuickChatFriendContactStoreImpl extends ServiceImpl<QuickChatFriendContactMapper, QuickChatContact> implements QuickChatFriendContactStore {
    @Override
    public List<QuickChatContact> getListByFromId(String fromId) {
        return this.lambdaQuery()
                .eq(QuickChatContact::getFromId, fromId)
                .list();
    }

    @Override
    public QuickChatContact getByFromIdAndToId(String fromId, String toId) {
        return this.lambdaQuery()
                .eq(QuickChatContact::getFromId, fromId)
                .eq(QuickChatContact::getToId, toId)
                .one();
    }

    @Override
    public Boolean deleteByFromIdAndToId(String fromId, String toId) {
        return lambdaUpdate()
                .eq(QuickChatContact::getFromId, fromId)
                .eq(QuickChatContact::getToId, toId)
                .remove();
    }
}
