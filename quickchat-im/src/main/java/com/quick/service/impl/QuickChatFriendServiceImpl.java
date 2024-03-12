package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatFriendMapper;
import com.quick.pojo.po.QuickChatFriend;
import com.quick.pojo.po.QuickChatUser;
import com.quick.service.QuickChatFriendService;
import com.quick.store.QuickChatFriendStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class QuickChatFriendServiceImpl extends ServiceImpl<QuickChatFriendMapper, QuickChatFriend> implements QuickChatFriendService {
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatFriendStore friendStore;

    @Override
    public List<QuickChatFriend> getFriendList() {
        String accountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        return friendStore.getListByFromId(accountId);
    }

    @Override
    public Boolean addFriend(String accountId) {
        // 查询用户信息是否存在
        QuickChatUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.USER_NOT_EXIST);
        }

        // 判断对方是否是你的好友
        return null;
    }

    @Override
    public Boolean deleteFriend(String accountId) {
        return null;
    }

    @Override
    public Boolean blockFriend(String accountId) {
        return null;
    }

    @Override
    public Boolean agreeApply(String accountId) {
        return null;
    }

    @Override
    public QuickChatFriend getByFromIdAndToId(String fromId, String toId) {
        return friendStore.getByFromIdAndToId(fromId, toId);
    }
}
