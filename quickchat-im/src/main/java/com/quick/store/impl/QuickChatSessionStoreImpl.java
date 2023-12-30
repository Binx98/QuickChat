package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatSessionMapper;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatSessionStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 聊天会话（针对单聊） 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatSessionStoreImpl extends ServiceImpl<QuickChatSessionMapper, QuickChatSession> implements QuickChatSessionStore {
    /**
     * 根据 account_id 查询会话列表
     */
    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_SESSION, key = "#p0")
    public List<QuickChatSession> getListByAccountId(String accountId) {
        return this.lambdaQuery()
                .eq(QuickChatSession::getFromId, accountId)
                .orderByDesc(QuickChatSession::getUpdateTime)
                .list();
    }

    /**
     * 删除会话
     */
    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_SESSION, allEntries = true)
    public Boolean deleteBySessionId(Long sessionId) {
        return this.removeById(sessionId);
    }

    /**
     * 通信双方 account_id 查询单条会话信息
     */
    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_SESSION, key = "#p0 + #p1")
    public QuickChatSession getOneByAccountId(String fromId, String toId) {
        return this.lambdaQuery()
                .eq(QuickChatSession::getFromId, fromId)
                .eq(QuickChatSession::getToId, toId)
                .one();
    }

    /**
     * 保存会话信息
     */
    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_SESSION, allEntries = true)
    public Boolean saveInfo(QuickChatSession chatSession) {
        return this.save(chatSession);
    }

    /**
     * 修改会话信息
     */
    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_SESSION, allEntries = true)
    public Boolean updateInfo(QuickChatSession chatSession) {
        return this.updateById(chatSession);
    }
}
