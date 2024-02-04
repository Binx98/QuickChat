package com.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.vo.UnreadCountVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 聊天会话（针对单聊） Mapper 接口
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
public interface QuickChatSessionMapper extends BaseMapper<QuickChatSession> {
    UnreadCountVO getUnreadCount(@Param("relationId") String relationId, @Param("lastReadTime") LocalDateTime lastReadTime);
}
