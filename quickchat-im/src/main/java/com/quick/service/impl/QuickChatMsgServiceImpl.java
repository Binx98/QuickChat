package com.quick.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.service.QuickChatMsgService;
import com.quick.store.QuickChatMsgStore;
import com.quick.utils.RelationUtil;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 聊天信息 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatMsgServiceImpl extends ServiceImpl<QuickChatMsgMapper, QuickChatMsg> implements QuickChatMsgService {
    @Autowired
    private QuickChatMsgStore msgStore;

    /**
     * 查询聊天记录
     */
    @Override
    public void getByRelationId(String accountId, Integer current, Integer size) {
        // 根据双方 account_id 生成 relation_id
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        String relationId = RelationUtil.generate(loginAccountId, accountId);

        // 分页查询聊天记录
        Page<QuickChatMsg> msgPage = msgStore.getByRelationId(relationId, current, size);
        List<QuickChatMsg> msgRecords = msgPage.getRecords();
        Collections.reverse(msgRecords);

        ChatMsgAdapter.buildChatMsgVOList(msgRecords);
    }

    /**
     * 查询双方聊天信息列表
     */
    @Override
    public Map<String, List<QuickChatMsg>> getMapByAccountIds(String loginAccountId, List<String> toAccountIds) {
        // 构建通信双方关联key，封装List
        Set<String> relationSet = new HashSet<>();
        for (String toAccountId : toAccountIds) {
            String relationId = RelationUtil.generate(loginAccountId, toAccountId);
            relationSet.add(relationId);
        }

        // 批量查询聊天信息
        for (String relationId : relationSet) {
        }

        return null;
    }
}
