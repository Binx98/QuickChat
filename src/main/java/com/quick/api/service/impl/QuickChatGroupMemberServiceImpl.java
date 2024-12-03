package com.quick.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.api.mapper.QuickChatGroupMemberMapper;
import com.quick.common.adapter.ApplyAdapter;
import com.quick.common.adapter.UserAdapter;
import com.quick.common.constant.RocketMQConstant;
import com.quick.common.enums.ApplyTypeEnum;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.enums.YesNoEnum;
import com.quick.common.exception.QuickException;
import com.quick.common.mq.producer.MyRocketMQTemplate;
import com.quick.common.pojo.po.QuickChatApply;
import com.quick.common.pojo.po.QuickChatGroup;
import com.quick.common.pojo.po.QuickChatGroupMember;
import com.quick.common.pojo.po.QuickChatUser;
import com.quick.common.pojo.vo.ChatUserVO;
import com.quick.api.service.QuickChatGroupMemberService;
import com.quick.api.store.QuickChatApplyStore;
import com.quick.api.store.QuickChatGroupMemberStore;
import com.quick.api.store.QuickChatGroupStore;
import com.quick.api.store.QuickChatUserStore;
import com.quick.common.utils.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private QuickChatGroupMemberStore memberStore;
    @Autowired
    private MyRocketMQTemplate rocketMQTemplate;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatApplyStore applyStore;
    @Autowired
    private QuickChatUserStore userStore;

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

        // 根据 accountIdList 查询群成员信息，如果已经在群中就不处理了
        List<QuickChatGroupMember> savedMemberList = memberStore.getGroupMemberByAccountIdList(groupId, accountIdList);
        List<String> savedMemberIdList = savedMemberList.stream()
                .map(QuickChatGroupMember::getAccountId)
                .collect(Collectors.toList());
        accountIdList.removeAll(savedMemberIdList);
        if (CollectionUtils.isEmpty(accountIdList)) {
            return;
        }

        List<QuickChatApply> applyList = new ArrayList<>();
        for (String accountId : accountIdList) {
            QuickChatApply apply = ApplyAdapter.buildFriendApplyPO(loginAccountId, accountId, "邀请您加入群聊: "
                    + chatGroup.getGroupName(), ApplyTypeEnum.GROUP.getCode(), groupId, YesNoEnum.NO.getCode());
            applyList.add(apply);
        }
        applyStore.saveAll(applyList);
        for (QuickChatApply apply : applyList) {
            applyStore.deleteCacheByApplyId(apply.getId());
        }
        rocketMQTemplate.asyncSend(RocketMQConstant.GROUP_APPLY_TOPIC, applyList);
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
        rocketMQTemplate.asyncSend(RocketMQConstant.GROUP_DELETE_MEMBER_NOTICE, member);
    }
}
