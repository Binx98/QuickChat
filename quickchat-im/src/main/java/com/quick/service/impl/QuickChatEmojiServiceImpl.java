package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatEmojiAdapter;
import com.quick.mapper.QuickChatEmojiMapper;
import com.quick.pojo.po.QuickChatEmoji;
import com.quick.service.QuickChatEmojiService;
import com.quick.store.QuickChatEmojiStore;
import com.quick.utils.RequestHolderUtil;
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

    /**
     * 查询该用户表情包列表
     */
    @Override
    public List<QuickChatEmoji> getEmojiList(String accountId) {
        return emojiStore.getEmojiList(accountId);
    }

    /**
     * 添加表情包
     */
    @Override
    public Boolean addEmoji(String url) {
        String accountId = (String) RequestHolderUtil.get().get("account_id");
        QuickChatEmoji chatEmoji = ChatEmojiAdapter.buildEmojiPO(url, accountId);
        return emojiStore.saveEmoji(chatEmoji);
    }
}
