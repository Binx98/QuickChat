package com.quick.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.api.mapper.QuickChatGroupMapper;
import com.quick.common.adapter.ContactAdapter;
import com.quick.common.adapter.GroupAdapter;
import com.quick.common.constant.RocketMQConstant;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.enums.SessionTypeEnum;
import com.quick.common.exception.QuickException;
import com.quick.common.mq.producer.MyRocketMQTemplate;
import com.quick.common.pojo.dto.GroupDTO;
import com.quick.common.pojo.po.QuickChatContact;
import com.quick.common.pojo.po.QuickChatGroup;
import com.quick.common.pojo.po.QuickChatGroupMember;
import com.quick.api.service.QuickChatGroupService;
import com.quick.api.store.QuickChatContactStore;
import com.quick.api.store.QuickChatGroupMemberStore;
import com.quick.api.store.QuickChatGroupStore;
import com.quick.api.store.QuickChatSessionStore;
import com.quick.common.utils.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 群聊 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
@Slf4j
@Service
public class QuickChatGroupServiceImpl extends ServiceImpl<QuickChatGroupMapper, QuickChatGroup> implements QuickChatGroupService {
    @Autowired
    private MyRocketMQTemplate rocketMQTemplate;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatContactStore contactStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(GroupDTO groupDTO) {
        QuickChatGroup groupPO = GroupAdapter.buildGroupPO(groupDTO);
        groupStore.saveGroup(groupPO);
        QuickChatContact contact = ContactAdapter.buildContactPO
                (groupDTO.getAccountId(), groupDTO.getGroupId(), SessionTypeEnum.GROUP.getCode());
        contactStore.saveContact(contact);
    }

    @Override
    public void releaseGroup(Long groupId) {
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroup groupPO = groupStore.getByGroupId(groupId);
        if (ObjectUtils.isEmpty(groupPO) || groupPO.getAccountId().equals(loginAccountId)) {
            throw new QuickException(ResponseEnum.NOT_GROUP_OWNER);
        }
        groupStore.dismissByGroupId(groupId);
        List<QuickChatGroupMember> members = memberStore.getListByGroupId(groupId);
        List<String> accountIds = members.stream()
                .map(QuickChatGroupMember::getAccountId)
                .collect(Collectors.toList());
        Map<String, Object> param = new HashMap<>();
        param.put("accountIds", accountIds);
        param.put("groupId", groupId);
        rocketMQTemplate.asyncSend(RocketMQConstant.GROUP_RELEASE_NOTICE, param);
    }

    @Override
    public void exitGroup(Long groupId) {
        QuickChatGroup group = groupStore.getByGroupId(groupId);
        if (ObjectUtils.isEmpty(group)) {
            throw new QuickException(ResponseEnum.GROUP_NOT_EXIST);
        }
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        memberStore.deleteByGroupIdAndAccountId(groupId, loginAccountId);
        sessionStore.deleteByFromIdAndToId(loginAccountId, String.valueOf(groupId));
    }

    @Override
    public void updateInfo(GroupDTO group) {
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroup groupPO = groupStore.getByGroupId(group.getGroupId());
        if (!groupPO.getAccountId().equals(loginAccountId)) {
            throw new QuickException(ResponseEnum.NOT_GROUP_OWNER);
        }
        groupPO.setGroupName(group.getGroupName());
        groupPO.setGroupAvatar(group.getGroupAvatar());
        groupPO.setInvitePermission(group.getInvitePermission());
        groupStore.updateInfo(groupPO);
    }
}
