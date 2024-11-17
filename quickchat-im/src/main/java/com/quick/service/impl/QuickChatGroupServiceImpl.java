package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ContactAdapter;
import com.quick.adapter.GroupAdapter;
import com.quick.constant.RocketMQConstant;
import com.quick.enums.ResponseEnum;
import com.quick.enums.SessionTypeEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.mq.MyRocketMQTemplate;
import com.quick.pojo.dto.GroupDTO;
import com.quick.pojo.po.QuickChatContact;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.service.QuickChatGroupService;
import com.quick.store.mysql.QuickChatContactStore;
import com.quick.store.mysql.QuickChatGroupMemberStore;
import com.quick.store.mysql.QuickChatGroupStore;
import com.quick.store.mysql.QuickChatSessionStore;
import com.quick.utils.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
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
