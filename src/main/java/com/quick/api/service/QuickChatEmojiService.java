package com.quick.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.common.pojo.po.QuickChatEmoji;

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
    /**
     * 获取表情包列表
     *
     * @param accountId 账号id
     * @return 表情包列表
     */
    List<QuickChatEmoji> getEmojiList(String accountId);

    /**
     * 添加表情包
     *
     * @param url 表情包URL
     * @return 执行结果
     */
    Boolean addEmoji(String url);

    /**
     * 删除表情包
     *
     * @param id 主键id
     * @return 执行结果
     */
    Boolean deleteEmoji(Long id);
}
