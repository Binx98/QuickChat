package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatEmoji;

import java.util.List;

/**
 * <p>
 * 表情包 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
public interface QuickChatEmojiService extends IService<QuickChatEmoji> {

    List<QuickChatEmoji> getEmojiList(String accountId);

    Boolean addEmoji(String url);
}
