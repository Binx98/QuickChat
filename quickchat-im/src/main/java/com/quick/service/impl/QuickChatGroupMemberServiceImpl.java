package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ApplyAdapter;
import com.quick.adapter.UserAdapter;
import com.quick.constant.RocketMQConstant;
import com.quick.enums.ApplyTypeEnum;
import com.quick.enums.ResponseEnum;
import com.quick.enums.YesNoEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatGroupMemberMapper;
import com.quick.pojo.po.QuickChatApply;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickChatGroupMemberService;
import com.quick.store.QuickChatApplyStore;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 群成员 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
@Slf4j
@Service
public class QuickChatGroupMemberServiceImpl extends ServiceImpl<QuickChatGroupMemberMapper, QuickChatGroupMember> implements QuickChatGroupMemberService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatApplyStore applyStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;
    @Value("${quick-chat.group.invite-count}")
    private Integer inviteCountLimit;


    @Override
    public List<ChatUserVO> getGroupMemberList(Long groupId) {
        List<QuickChatGroupMember> members = memberStore.getListByGroupId(groupId);
        List<String> accountIdList = members.stream()
                .map(QuickChatGroupMember::getAccountId)
                .collect(Collectors.toList());
        List<QuickChatUser> userList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accountIdList)) {
            userList = userStore.getListByAccountIds(accountIdList);
        }
        return UserAdapter.buildUserVOList(userList);
    }

    @Override
    public void addMember(Long groupId, List<String> accountIdList) {
        if (CollectionUtils.isEmpty(accountIdList)) {
            throw new QuickException(ResponseEnum.GROUP_MEMBER_COUNT_NOT_EXIST);
        }
        if (CollectionUtils.isNotEmpty(accountIdList) && accountIdList.size() > inviteCountLimit) {
            throw new QuickException(ResponseEnum.GROUP_MEMBER_ADD_COUNT_NOT_ALLOW);
        }
        QuickChatGroup chatGroup = groupStore.getByGroupId(groupId);
        if (ObjectUtils.isEmpty(chatGroup)) {
            throw new QuickException(ResponseEnum.GROUP_NOT_EXIST);
        }
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        if (YesNoEnum.NO.getCode().equals(chatGroup.getInvitePermission())
                && !loginAccountId.equals(chatGroup.getAccountId())) {
            throw new QuickException(ResponseEnum.GROUP_MEMBER_NOT_ALLOW);
        }

        List<QuickChatGroupMember> groupMemberByAccountId = memberStore.getGroupMemberByAccountId(groupId, accountIdList);
        List<String> savedAccountIdList = groupMemberByAccountId.stream()
                .map(QuickChatGroupMember::getAccountId)
                .collect(Collectors.toList());
        accountIdList.removeAll(savedAccountIdList);
        List<QuickChatApply> applyList = new ArrayList<>();
        for (String accountId : accountIdList) {
            QuickChatApply apply = ApplyAdapter.buildFriendApplyPO(loginAccountId, accountId, "邀请您加入群聊: "
                    + chatGroup.getGroupName(), ApplyTypeEnum.GROUP.getCode(), groupId, YesNoEnum.NO.getCode());
            applyList.add(apply);
        }
        applyStore.saveAll(applyList);
        rocketMQTemplate.asyncSend(RocketMQConstant.GROUP_APPLY_TOPIC, MessageBuilder.withPayload(applyList).build(),
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("-------------rocketmq message send successful: {}------------", sendResult);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        log.error("-------------rocketmq message send failed: {}------------", throwable.toString());
                    }
                }
        );
    }

    @Override
    public void deleteMember(Long groupId, String accountId) {
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroup groupPO = groupStore.getByGroupId(groupId);
        if (ObjectUtils.isEmpty(groupPO) || groupPO.getAccountId().equals(loginAccountId)) {
            throw new QuickException(ResponseEnum.NOT_GROUP_OWNER);
        }
        QuickChatGroupMember member = memberStore.getMemberByAccountId(groupId, accountId);
        memberStore.deleteByGroupIdAndAccountId(groupId, accountId);
        rocketMQTemplate.asyncSend(RocketMQConstant.GROUP_DELETE_MEMBER_NOTICE, MessageBuilder.withPayload(member).build(),
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("-------------rocketmq message send successful: {}------------", sendResult);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        log.error("-------------rocketmq message send failed: {}------------", throwable.toString());
                    }
                }
        );
    }
}
