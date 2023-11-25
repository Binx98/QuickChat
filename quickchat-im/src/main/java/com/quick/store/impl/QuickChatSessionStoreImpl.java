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
                .eq(QuickChatSession::getSendId, accountId)
                .orderByDesc(QuickChatSession::getUpdateTime)
                .list();
    }

    /**
     * 修改会话未读数量
     */
    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_SESSION, key = "getListByAccountId")
    public Boolean updateUnreadBySessionId(Long sessionId, int count) {
        return this.lambdaUpdate()
                .eq(QuickChatSession::getId, sessionId)
                .set(QuickChatSession::getUnreadCount, count)
                .update();
    }

    /**
     * 删除会话
     */
    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_SESSION, key = "getListByAccountId")
    public Boolean deleteBySessionId(Long sessionId) {
        return this.removeById(sessionId);
    }
}
