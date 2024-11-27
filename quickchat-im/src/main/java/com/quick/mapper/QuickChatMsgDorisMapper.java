package com.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.pojo.po.QuickChatMsg;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface QuickChatMsgDorisMapper extends BaseMapper<QuickChatMsg> {

}
