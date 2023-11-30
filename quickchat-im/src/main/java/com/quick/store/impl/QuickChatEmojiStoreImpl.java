package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatEmojiMapper;
import com.quick.pojo.po.QuickChatEmoji;
import com.quick.store.QuickChatEmojiStore;
import org.springframework.stereotype.Service;

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

}
