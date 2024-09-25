package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatContactMapper;
import com.quick.pojo.po.QuickChatContact;
import com.quick.store.QuickChatContactStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 通讯录 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Service
public class QuickChatFriendContactStoreImpl extends ServiceImpl<QuickChatContactMapper, QuickChatContact> implements QuickChatContactStore {
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
        return this.lambdaUpdate()
                .eq(QuickChatContact::getFromId, fromId)
                .eq(QuickChatContact::getToId, toId)
                .remove();
    }

    @Override
    public Boolean saveContact(QuickChatContact contact) {
        return this.save(contact);
    }

    @Override
    public Boolean saveContactList(List<QuickChatContact> contacts) {
        return this.saveBatch(contacts);
    }

    @Override
    public Boolean updateContact(QuickChatContact friendPO) {
        return this.updateById(friendPO);
    }
}
