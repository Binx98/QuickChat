package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.service.QuickChatGroupService;
import com.quick.store.QuickChatGroupStore;
import com.quick.utils.RequestContextUtil;
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
    private QuickChatGroupStore groupStore;

    @Override
    public List<QuickChatGroup> getGroupList() {
        String accountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        return groupStore.getListByAccountId(accountId);
    }
}
