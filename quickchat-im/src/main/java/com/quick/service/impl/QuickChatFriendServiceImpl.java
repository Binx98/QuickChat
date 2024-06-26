package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.UserAdapter;
import com.quick.mapper.QuickChatFriendMapper;
import com.quick.pojo.po.QuickChatFriend;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickChatFriendService;
import com.quick.store.QuickChatFriendStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private QuickChatFriendStore friendStore;
    @Autowired
    private QuickChatUserStore userStore;

    @Override
    public List<ChatUserVO> getFriendList() {
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatFriend> friendList = friendStore.getListByFromId(loginAccountId);
        List<String> accountIds = friendList.stream()
                .map(item -> item.getToId())
                .collect(Collectors.toList());
        List<QuickChatUser> userList = userStore.getListByAccountIds(accountIds);
        return UserAdapter.buildUserVOList(userList);
    }
}
