package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.GroupMemberAdapter;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatGroupMemberMapper;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatUser;
import com.quick.service.QuickChatGroupMemberService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class QuickChatGroupMemberServiceImpl extends ServiceImpl<QuickChatGroupMemberMapper, QuickChatGroupMember> implements QuickChatGroupMemberService {
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    public List<QuickChatUser> getMemberByGroupId(String groupId) {
        List<QuickChatGroupMember> memberList = memberStore.getByGroupId(groupId);
        List<String> accountIds = memberList.stream()
                .map(QuickChatGroupMember::getAccountId)
                .collect(Collectors.toList());
        // TODO 设置了群昵称备注
        return userStore.getListByAccountIds(accountIds);
    }

    @Override
    public Boolean enterGroup(String groupId) {
        String accountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroupMember memberPO = GroupMemberAdapter.buildMemberPO(groupId, accountId);
        return memberStore.enterGroup(memberPO);
    }

    @Override // TODO 分布式锁！！！！！
    public Boolean exitGroup(String groupId, String accountId) {
        // 判断群组是否存在
        QuickChatGroup groupPO = groupStore.getByGroupId(groupId);
        if (ObjectUtils.isEmpty(groupPO)) {
            throw new QuickException(ResponseEnum.GROUP_NOT_EXIST);
        }

        // 退出群聊，删除群成员信息
        memberStore.deleteByGroupIdAndAccountId(groupId, accountId);

        // 群成员数量 - 1
        groupPO.setMemberCount(groupPO.getMemberCount() - 1);
        groupStore.updateInfo(groupPO);

        // 删除会话框
        return sessionStore.deleteByFromIdAndToId(accountId, groupId);
    }
}
