package com.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.pojo.po.QuickChatEmoji;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 表情包 Mapper 接口
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Mapper
public interface QuickChatEmojiMapper extends BaseMapper<QuickChatEmoji> {

}
