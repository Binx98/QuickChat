package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatGroupMemberMapper;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatUser;
import com.quick.service.QuickChatGroupMemberService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatUserStore;
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
    private QuickChatGroupMemberStore memberStore;

    /**
     * 查询群成员列表
     */
    @Override
    public List<QuickChatUser> getMemberByGroupId(String groupId) {
        List<QuickChatGroupMember> memberList = memberStore.getByGroupId(groupId);
        List<String> accountIds = memberList.stream().map(QuickChatGroupMember::getAccountId).collect(Collectors.toList());
        return userStore.getListByAccountIds(accountIds);
    }
}
