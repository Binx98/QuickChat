package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatSessionMapper;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.vo.UnreadCountVO;
import com.quick.store.QuickChatSessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private QuickChatSessionMapper sessionMapper;

    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_SESSION, key = "#p0", unless = "#result.size() == 0")
    public List<QuickChatSession> getListByAccountId(String accountId) {
        return this.lambdaQuery()
                .eq(QuickChatSession::getFromId, accountId)
                .orderByDesc(QuickChatSession::getUpdateTime)
                .list();
    }

    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_SESSION, allEntries = true)
    public Boolean deleteBySessionId(Long sessionId) {
        return this.removeById(sessionId);
    }

    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_SESSION, key = "#p0 + #p1")
    public QuickChatSession getByAccountId(String fromId, String toId) {
        return this.lambdaQuery()
                .eq(QuickChatSession::getFromId, fromId)
                .eq(QuickChatSession::getToId, toId)
                .one();
    }

    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_SESSION, allEntries = true)
    public Boolean saveInfo(QuickChatSession chatSession) {
        return this.save(chatSession);
    }

    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_SESSION, allEntries = true)
    public Boolean updateInfo(QuickChatSession chatSession) {
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
    public Boolean saveList(List<QuickChatSession> sessionPOList) {
        return this.saveBatch(sessionPOList);
    }

    @Override
    public UnreadCountVO getUnreadCount(String relationId, LocalDateTime lastReadTime) {
        return sessionMapper.getUnreadCount(relationId, lastReadTime);
    }
}
