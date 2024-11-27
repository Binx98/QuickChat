package com.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.pojo.po.QuickChatSession;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 聊天会话（针对单聊） Mapper 接口
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Mapper
public interface QuickChatSessionMapper extends BaseMapper<QuickChatSession> {

}
