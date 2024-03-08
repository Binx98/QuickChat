package com.quick.store;

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
public interface QuickChatEmojiStore extends IService<QuickChatEmoji> {
    /**
     * 根据 account_id 查询表情包列表
     *
     * @param accountId 账号id
     * @return 表情包List
     */
    List<QuickChatEmoji> getEmojiList(String accountId);

    /**
     * 保存表情包
     *
     * @param chatEmoji 表情包实体
     * @return 执行结果
     */
    Boolean saveEmoji(QuickChatEmoji chatEmoji);

    /**
     * 删除表情包
     *
     * @param emojiId   表情包id
     * @param accountId 账户id
     * @return 执行结果
     */
    Boolean deleteByEmojiId(Long emojiId, String accountId);
}
