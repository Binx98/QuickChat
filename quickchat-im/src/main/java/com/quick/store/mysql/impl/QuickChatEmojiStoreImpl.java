package com.quick.store.mysql.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatEmojiMapper;
import com.quick.pojo.po.QuickChatEmoji;
import com.quick.store.mysql.QuickChatEmojiStore;
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
@DS("mysql")
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
