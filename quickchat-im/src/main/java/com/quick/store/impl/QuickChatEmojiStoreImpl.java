package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatEmojiMapper;
import com.quick.pojo.po.QuickChatEmoji;
import com.quick.store.QuickChatEmojiStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 表情包 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Service
public class QuickChatEmojiStoreImpl extends ServiceImpl<QuickChatEmojiMapper, QuickChatEmoji> implements QuickChatEmojiStore {
    /**
     * 查询该用户表情包列表
     */
    @Override
    @Cacheable(value = RedisConstant.QUICK_CHAT_EMOJI, key = "#p0")
    public List<QuickChatEmoji> getEmojiList(String accountId) {
        return this.lambdaQuery()
                .eq(QuickChatEmoji::getAccountId, accountId)
                .list();
    }

    /**
     * 添加表情包
     */
    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_EMOJI, key = "#p0.accountId")
    public Boolean saveEmoji(QuickChatEmoji chatEmoji) {
        return this.save(chatEmoji);
    }

    /**
     * 删除表情包
     */
    @Override
    @CacheEvict(value = RedisConstant.QUICK_CHAT_EMOJI, key = "#p1")
    public Boolean deleteByEmojiId(Long emojiId, String accountId) {
        return this.removeById(emojiId);
    }
}
