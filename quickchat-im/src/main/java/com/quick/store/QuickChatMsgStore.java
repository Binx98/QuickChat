package com.quick.store;

import com.quick.pojo.po.QuickChatMsg;
import com.baomidou.mybatisplus.extension.service.IService;

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

    List<QuickChatMsg> getChatMsg(String loginAccountId, String accountId);
}
