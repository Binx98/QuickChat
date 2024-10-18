package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatContactMapper;
import com.quick.pojo.po.QuickChatContact;
import com.quick.store.QuickChatContactStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @Cacheable(value = RedisConstant.QUICK_CHAT_FRIEND_CONTACT, key = "'getListByFromId:' + #p0", unless = "#result.isEmpty()")
    public List<QuickChatContact> getListByFromId(String fromId) {
        return this.lambdaQuery()
                .eq(QuickChatContact::getFromId, fromId)
                .list();
    }

    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_FRIEND_CONTACT, key = "'getByFromIdAndToId:' + #p0 + #p1", unless = "#result == null")
    public QuickChatContact getByFromIdAndToId(String fromId, String toId) {
        return this.lambdaQuery()
                .eq(QuickChatContact::getFromId, fromId)
                .eq(QuickChatContact::getToId, toId)
                .one();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_FRIEND_CONTACT, key = "'getListByFromId:' + #p0"),
            @CacheEvict(value = RedisConstant.QUICK_CHAT_FRIEND_CONTACT, key = "'getByFromIdAndToId:' + #p0 + #p1")
    })
    public Boolean deleteByFromIdAndToId(String fromId, String toId) {
        return this.lambdaUpdate()
                .eq(QuickChatContact::getFromId, fromId)
                .eq(QuickChatContact::getToId, toId)
                .remove();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_FRIEND_CONTACT, key = "'getListByFromId:' + #p0.fromId"),
    })
    public Boolean saveContact(QuickChatContact contact) {
        return this.save(contact);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_FRIEND_CONTACT, key = "'getListByFromId:' + #p0.fromId"),
    })
    public Boolean updateContact(QuickChatContact friendPO) {
        return this.updateById(friendPO);
    }

    @Override
    public Boolean saveContactList(List<QuickChatContact> contacts) {
        return this.saveBatch(contacts);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_FRIEND_CONTACT, key = "'getListByFromId:' + #p0")
    })
    public void deleteCacheByFromIdAndToId(String fromId, String toId) {
    }
}
