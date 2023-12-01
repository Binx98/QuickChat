package com.quick.adapter;

import com.quick.pojo.po.QuickChatEmoji;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-12-01  09:18
 * @Description: 表情包适配器
 * @Version: 1.0
 */
public class ChatEmojiAdapter {

    public static QuickChatEmoji buildEmojiPO(String url, String accountId) {
        return QuickChatEmoji.builder()
                .accountId(accountId)
                .url(url)
                .build();
    }
}
