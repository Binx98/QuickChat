package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatSessionMapper;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatSessionStore;
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
    @Cacheable(value = RedisConstant.QUICK_CHAT_MSG)
    public List<QuickChatSession> getListByAccountId(String accountId) {
        return null;
    }
}
