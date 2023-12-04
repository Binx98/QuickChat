package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.enums.YesNoEnum;
import com.quick.mapper.QuickChatFriendMapper;
import com.quick.pojo.po.QuickChatFriend;
import com.quick.store.QuickChatFriendStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 聊天好友 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Service
public class QuickChatFriendStoreImpl extends ServiceImpl<QuickChatFriendMapper, QuickChatFriend> implements QuickChatFriendStore {
    /**
     * 查询好友列表
     */
    @Override
    public List<QuickChatFriend> getListByAccountId(String accountId) {
        return null;
    }
}
