package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.UserAdapter;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatFriendMapper;
import com.quick.pojo.po.QuickChatFriendContact;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickChatFriendService;
import com.quick.store.QuickChatFriendApplyStore;
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
public class QuickChatFriendServiceImpl extends ServiceImpl<QuickChatFriendMapper, QuickChatFriendContact> implements QuickChatFriendService {
    @Autowired
    private QuickChatFriendApplyStore applyStore;
    @Autowired
    private QuickChatFriendStore friendStore;
    @Autowired
    private QuickChatUserStore userStore;

    @Override
    public List<ChatUserVO> getFriendList() {
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatFriendContact> friendList = friendStore.getListByFromId(loginAccountId);
        List<String> accountIds = friendList.stream()
                .map(item -> item.getToId())
                .collect(Collectors.toList());
        List<QuickChatUser> userList = userStore.getListByAccountIds(accountIds);
        return UserAdapter.buildUserVOList(userList);
    }

    @Override
    public Boolean addFriend(String toId) {
        // 查询当前用户是否是好友
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatFriendContact friendPO = friendStore.getByFromIdAndToId(loginAccountId, toId);
        if (ObjectUtils.isNotEmpty(friendPO)) {
            throw new QuickException(ResponseEnum.IS_YOUR_FRIEND);
        }

        // 保存好友申请记录

        // 推送给目标用户
        return null;
    }
}
