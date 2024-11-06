package com.quick.store.mysql.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatSessionMapper;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.mysql.QuickChatSessionStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
@DS("mysql")
public class QuickChatSessionStoreImpl extends ServiceImpl<QuickChatSessionMapper, QuickChatSession> implements QuickChatSessionStore {
    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_SESSION, key = "'getListByAccountId:' + #p0", unless = "#result.isEmpty()")
    public List<QuickChatSession> getListByAccountId(String accountId) {
        return this.lambdaQuery()
                .eq(QuickChatSession::getFromId, accountId)
                .orderByDesc(QuickChatSession::getUpdateTime)
                .list();
    }

    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_SESSION, key = "'getByFromIdAndToId:' + #p0 + #p1", unless = "#result == null")
    public QuickChatSession getByFromIdAndToId(String fromId, String toId) {
        return this.lambdaQuery()
                .eq(QuickChatSession::getFromId, fromId)
                .eq(QuickChatSession::getToId, toId)
                .one();
    }

    @Override
    public Boolean saveInfo(QuickChatSession chatSession) {
        return this.save(chatSession);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstant.QUICK_CHAT_SESSION, key = "'getByFromIdAndToId:' + #p0.fromId + #p0.toId")
    })
    public Boolean updateSessionById(QuickChatSession chatSession) {
        return this.updateById(chatSession);
    }

    @Override
    public List<QuickChatSession> getListByAccountIdList(List<String> fromIds, String toId) {
        return this.lambdaQuery()
                .eq(QuickChatSession::getToId, toId)
                .in(QuickChatSession::getFromId, fromIds)
                .list();
    }

    @Override
    public Boolean saveSessionList(List<QuickChatSession> sessionPOList) {
        return this.saveBatch(sessionPOList);
    }

    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_SESSION, key = "'getBySessionId:' + #p0", unless = "#result == null")
    public QuickChatSession getBySessionId(Long sessionId) {
        return this.getById(sessionId);
    }

    @Override
    public Boolean deleteByFromIdAndToId(String fromId, String toId) {
        return this.lambdaUpdate()
                .eq(QuickChatSession::getFromId, fromId)
                .eq(QuickChatSession::getToId, toId)
                .remove();
    }

    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_SESSION, key = "'getAllBySessionId:' + #p0", unless = "#result.isEmpty()")
    public List<QuickChatSession> getAllBySessionId(Long relationId) {
        return this.lambdaQuery()
                .eq(QuickChatSession::getRelationId, relationId)
                .in(QuickChatSession::getDeleted, true, false)
                .list();
    }

    @Override
    public Boolean updateList(List<QuickChatSession> sessionList) {
        return this.updateBatchById(sessionList);
    }
}
