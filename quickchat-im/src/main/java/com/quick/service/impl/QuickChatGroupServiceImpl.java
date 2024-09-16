package com.quick.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ContactAdapter;
import com.quick.adapter.GroupAdapter;
import com.quick.constant.KafkaConstant;
import com.quick.enums.ResponseEnum;
import com.quick.enums.SessionTypeEnum;
import com.quick.exception.QuickException;
import com.quick.kafka.KafkaProducer;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.pojo.dto.GroupDTO;
import com.quick.pojo.po.QuickChatContact;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.service.QuickChatGroupService;
import com.quick.store.QuickChatContactStore;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.utils.RequestContextUtil;
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
@Service
public class QuickChatGroupServiceImpl extends ServiceImpl<QuickChatGroupMapper, QuickChatGroup> implements QuickChatGroupService {
    @Autowired
    private KafkaProducer kafkaProducer;
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
    public Boolean createGroup(GroupDTO groupDTO) {
        QuickChatGroup groupPO = GroupAdapter.buildGroupPO(groupDTO);
        groupStore.saveGroup(groupPO);
        QuickChatContact contact = ContactAdapter.buildContactPO
                (groupDTO.getAccountId(), groupDTO.getGroupId(), SessionTypeEnum.GROUP.getCode(), null);
        return contactStore.saveContact(contact);
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
        kafkaProducer.send(KafkaConstant.GROUP_RELEASE_NOTICE, JSONUtil.toJsonStr(param));
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
