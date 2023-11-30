package com.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.pojo.po.QuickChatMsg;
import org.apache.ibatis.annotations.Select;

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
    /**
     * 查询通讯双方聊天记录
     */
    @Select("")
    List<QuickChatMsg> getChatMsgList();
}
