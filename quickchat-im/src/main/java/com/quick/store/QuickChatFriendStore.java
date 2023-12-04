package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatFriend;

import java.util.List;

/**
 * <p>
 * 聊天好友 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
public interface QuickChatFriendStore extends IService<QuickChatFriend> {

    List<QuickChatFriend> getListByAccountId(String accountId);
}
