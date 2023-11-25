package com.quick.store;

import com.quick.pojo.QuickChatMsg;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
