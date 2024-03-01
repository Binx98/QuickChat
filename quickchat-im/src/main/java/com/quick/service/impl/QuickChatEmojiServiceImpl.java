package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatEmojiAdapter;
import com.quick.mapper.QuickChatEmojiMapper;
import com.quick.pojo.po.QuickChatEmoji;
import com.quick.service.QuickChatEmojiService;
import com.quick.store.QuickChatEmojiStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class QuickChatEmojiServiceImpl extends ServiceImpl<QuickChatEmojiMapper, QuickChatEmoji> implements QuickChatEmojiService {
    @Autowired
    private QuickChatEmojiStore emojiStore;

    @Override
    public List<QuickChatEmoji> getEmojiList(String accountId) {
        return emojiStore.getEmojiList(accountId);
    }

    @Override
    public Boolean addEmoji(String url) {
        String accountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatEmoji chatEmoji = ChatEmojiAdapter.buildEmojiPO(url, accountId);
        return emojiStore.saveEmoji(chatEmoji);
    }

    @Override
    public Boolean deleteEmoji(Long id) {
        String accountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        return emojiStore.deleteByEmojiId(id, accountId);
    }
}
