package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.GroupAdapter;
import com.quick.adapter.GroupMemberAdapter;
import com.quick.adapter.UserAdapter;
import com.quick.constant.KafkaConstant;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.kafka.KafkaProducer;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.pojo.dto.GroupDTO;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatGroupContact;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickChatGroupService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createGroup(GroupDTO groupDTO) {
        // 保存群聊信息
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroup groupPO = GroupAdapter.buildGroupPO(groupDTO);
        groupPO.setAccountId(loginAccountId);
        groupStore.saveGroup(groupPO);

        // 批量添加群成员
        List<String> accountIds = groupDTO.getAccountIdList();
        List<QuickChatGroupMember> memberList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accountIds)) {
            for (String accountId : accountIds) {
                QuickChatGroupMember member = GroupMemberAdapter.buildMemberPO(groupPO.getId(), accountId);
                memberList.add(member);
            }
            memberStore.saveMemberList(memberList);
        }

        // TODO 批量生成会话信息
//        sessionStore.saveList();

        // TODO 批量创建群组通讯录

        // Channel 通知邀请加入群聊
        kafkaProducer.send(KafkaConstant.FRIEND_APPLY_TOPIC, null);
        return null;
    }

    @Override
    public List<ChatUserVO> getGroupMemberList(Long groupId) {
        List<QuickChatGroupMember> members = memberStore.getListByGroupId(groupId);
        List<String> accountIdList = members.stream()
                .map(QuickChatGroupMember::getAccountId)
                .collect(Collectors.toList());
        List<QuickChatUser> userList = userStore.getListByAccountIds(accountIdList);
        return UserAdapter.buildUserVOList(userList);
    }

    @Override
    public Boolean addMember(Long groupId, List<String> accountIdList) {
        return null;
    }

    @Override
    public Boolean removeMember(Long groupId, String accountId) {
        // 判断当前操作是否是群主
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroup groupPO = groupStore.getByGroupId(groupId.toString());
        if (ObjectUtils.isEmpty(groupPO) || groupPO.getAccountId().equals(loginAccountId)) {
            throw new QuickException(ResponseEnum.FAIL);
        }

        // 删除群成员
        memberStore.deleteByGroupIdAndAccountId(groupId, accountId);

        // Channel 通知目标用户被移除群聊
        kafkaProducer.send(KafkaConstant.SEND_CHAT_GROUP_MSG, null);
        return null;
    }

    @Override
    public Boolean removeGroup(Long groupId) {
        // 判断当前操作是否是群主
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroup groupPO = groupStore.getByGroupId(groupId.toString());
        if (ObjectUtils.isEmpty(groupPO) || groupPO.getAccountId().equals(loginAccountId)) {
            throw new QuickException(ResponseEnum.FAIL);
        }

        // Channel 通知群内所有用户被当前群聊解散
        kafkaProducer.send(KafkaConstant.SEND_CHAT_GROUP_MSG, null);
        return null;
    }

    @Override
    public Boolean exitGroup(Long groupId) {
        // 删除群成员
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        memberStore.deleteByGroupIdAndAccountId(groupId, loginAccountId);

        // 删除会话
        return sessionStore.deleteByFromIdAndToId(loginAccountId, String.valueOf(groupId));
    }

    @Override
    public Boolean updateInfo(GroupDTO group) {
        // 判断当前操作是否是群主
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroup groupPO = groupStore.getByGroupId(group.getGroupId().toString());
        if (!groupPO.getAccountId().equals(loginAccountId)) {
            throw new QuickException(ResponseEnum.NOT_GROUP_OWNER);
        }

        // 修改群组信息
        groupPO.setGroupName(group.getGroupName());
        return groupStore.updateInfo(groupPO);
    }

    @Override
    public List<QuickChatGroupContact> getGroupContactList() {

        return null;
    }
}
