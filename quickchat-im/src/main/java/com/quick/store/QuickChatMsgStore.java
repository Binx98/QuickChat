package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatMsg;

import java.util.List;

/**
 * <p>
 * 聊天信息 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
public interface QuickChatMsgStore extends IService<QuickChatMsg> {

    Boolean saveMsg(QuickChatMsg chatMsg);

    List<QuickChatMsg> getMsgByRelationId(String relationId);
}
