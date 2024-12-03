package com.quick.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.api.mapper.QuickChatContactMapper;
import com.quick.common.adapter.ApplyAdapter;
import com.quick.common.adapter.UserAdapter;
import com.quick.common.constant.RocketMQConstant;
import com.quick.common.enums.ApplyTypeEnum;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.enums.YesNoEnum;
import com.quick.common.exception.QuickException;
import com.quick.common.mq.producer.MyRocketMQTemplate;
import com.quick.common.pojo.po.QuickChatApply;
import com.quick.common.pojo.po.QuickChatContact;
import com.quick.common.pojo.po.QuickChatUser;
import com.quick.common.pojo.vo.ChatUserVO;
import com.quick.api.service.QuickChatContactService;
import com.quick.api.store.QuickChatApplyStore;
import com.quick.api.store.QuickChatContactStore;
import com.quick.api.store.QuickChatSessionStore;
import com.quick.api.store.QuickChatUserStore;
import com.quick.common.utils.RequestContextUtil;
import com.quick.common.utils.SensitiveWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 通讯录 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Slf4j
@Service
public class QuickChatContactServiceImpl extends ServiceImpl<QuickChatContactMapper, QuickChatContact> implements QuickChatContactService {
    @Autowired
    private QuickChatContactStore friendContactStore;
    @Autowired
    private MyRocketMQTemplate rocketMQTemplate;
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatApplyStore applyStore;
    @Autowired
    private QuickChatUserStore userStore;

    @Override
    public List<ChatUserVO> getContactList() {
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatContact> friendList = friendContactStore.getListByFromId(loginAccountId);
        List<String> accountIds = friendList.stream()
                .map(item -> item.getToId())
                .collect(Collectors.toList());
        List<QuickChatUser> userList = userStore.getListByAccountIds(accountIds);
        return UserAdapter.buildUserVOList(userList);
    }

    @Override
    public void addFriend(String toId, String applyInfo) {
        String fromId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        QuickChatContact friendPO = friendContactStore.getByFromIdAndToId(fromId, toId);
        if (ObjectUtils.isNotEmpty(friendPO)) {
            throw new QuickException(ResponseEnum.YOUR_FRIEND);
        }
        QuickChatApply apply = ApplyAdapter.buildFriendApplyPO(fromId, toId,
                applyInfo, ApplyTypeEnum.FRIEND.getCode(), YesNoEnum.NO.getCode());
        applyStore.saveApply(apply);
        rocketMQTemplate.asyncSend(RocketMQConstant.FRIEND_APPLY_TOPIC, apply);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(String toId) {
        String fromId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        QuickChatContact friendPO = friendContactStore.getByFromIdAndToId(fromId, toId);
        if (ObjectUtils.isEmpty(friendPO)) {
            throw new QuickException(ResponseEnum.NOT_YOUR_FRIEND);
        }
        friendContactStore.deleteByFromIdAndToId(fromId, toId);
        friendContactStore.deleteByFromIdAndToId(toId, fromId);
        sessionStore.deleteByFromIdAndToId(fromId, toId);
    }

    @Override
    public void noteFriend(String toId, String noteName) {
        if (sensitiveWordUtil.check(noteName)) {
            throw new QuickException(ResponseEnum.NICK_NAME_NOT_ALLOW);
        }
        String fromId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        QuickChatContact friendPO = friendContactStore.getByFromIdAndToId(fromId, toId);
        if (ObjectUtils.isEmpty(friendPO)) {
            throw new QuickException(ResponseEnum.NOT_YOUR_FRIEND);
        }
        friendPO.setNoteName(noteName);
        friendContactStore.updateContact(friendPO);
    }
}
