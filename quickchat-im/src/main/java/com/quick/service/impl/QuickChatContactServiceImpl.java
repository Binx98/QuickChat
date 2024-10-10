package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ApplyAdapter;
import com.quick.adapter.UserAdapter;
import com.quick.constant.RocketMQConstant;
import com.quick.enums.ApplyTypeEnum;
import com.quick.enums.ResponseEnum;
import com.quick.enums.YesNoEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatContactMapper;
import com.quick.pojo.po.QuickChatApply;
import com.quick.pojo.po.QuickChatContact;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickChatContactService;
import com.quick.store.QuickChatApplyStore;
import com.quick.store.QuickChatContactStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import com.quick.utils.SensitiveWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
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
    private RocketMQTemplate rocketMQTemplate;
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
        rocketMQTemplate.asyncSend(RocketMQConstant.FRIEND_APPLY_TOPIC, MessageBuilder.withPayload(apply).build(),
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("-------------rocketmq message send successful: {}------------", sendResult);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        log.error("-------------rocketmq message send failed: {}------------", throwable.toString());
                    }
                });
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
