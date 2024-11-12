package com.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.pojo.po.QuickChatMsg;
import org.apache.ibatis.annotations.Param;

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
     * 根据 消息id列表 物理删除聊天记录
     *
     * @param ids 消息id列表
     * @return 执行结果
     */
    Boolean physicalDeleteMsgList(@Param("ids") List<Long> ids);
}
