package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.service.QuickChatGroupService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatMsgStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private QuickChatMsgStore msgStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    public List<QuickChatGroup> getGroupList() {
        String accountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        return groupStore.getListByAccountId(accountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean dismiss(String groupId) {
        // 查询群聊信息
        QuickChatGroup groupPO = groupStore.getByGroupId(groupId);
        if (ObjectUtils.isEmpty(groupPO)) {
            throw new QuickException(ResponseEnum.GROUP_NOT_EXIST);
        }

        // 判断是否是群主操作
        String accountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        if (accountId.equals(groupPO.getAccountId())) {
            throw new QuickException(ResponseEnum.NOT_GROUP_OWNER);
        }

        // 解散群聊
        groupStore.dismissByGroupId(groupId);

        // 删除群成员
        memberStore.deleteByGroupId(groupId);

        // 删除群聊天记录
        return msgStore.deleteByToId(groupId);
    }
}
