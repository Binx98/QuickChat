package com.quick.api.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.api.mapper.QuickChatEmojiMapper;
import com.quick.common.pojo.po.QuickChatEmoji;
import com.quick.api.store.QuickChatEmojiStore;
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
    @Override
    public List<QuickChatEmoji> getEmojiList(String accountId) {
        return this.lambdaQuery()
                .eq(QuickChatEmoji::getAccountId, accountId)
                .list();
    }

    @Override
    public Boolean saveEmoji(QuickChatEmoji chatEmoji) {
        return this.save(chatEmoji);
    }

    @Override
    public Boolean deleteByEmojiId(Long emojiId, String accountId) {
        return this.removeById(emojiId);
    }
}
