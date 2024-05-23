package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.pojo.dto.GroupDTO;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.service.QuickChatGroupService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    public Boolean createGroup(GroupDTO group) {
        // 保存群聊信息

        // 批量添加群成员
        List<String> accountIds = group.getAccountIdList();
        if (CollectionUtils.isNotEmpty(accountIds)) {
        }
        return null;
    }
}
