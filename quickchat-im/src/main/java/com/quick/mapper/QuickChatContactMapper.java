package com.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.pojo.po.QuickChatContact;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 聊天好友 Mapper 接口
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Mapper
public interface QuickChatContactMapper extends BaseMapper<QuickChatContact> {

}
