package com.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.pojo.po.QuickChatMsg;

import java.util.List;

/**
 * <p>
 * 聊天信息 Mapper 接口
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
public interface QuickChatMsgMapper extends BaseMapper<QuickChatMsg> {
    List<QuickChatMsg> getByRelationIdList(List<String> relationIds);
}
