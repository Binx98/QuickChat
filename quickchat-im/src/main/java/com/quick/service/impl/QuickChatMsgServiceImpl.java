package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public List<QuickChatMsg> getByRelationId(String accountId, Integer current, Integer size) {
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        String relationId = RelationUtil.generate(loginAccountId, accountId);
        Page<QuickChatMsg> msgPage = msgStore.getByRelationId(relationId, current, size);
        List<QuickChatMsg> msgRecords = msgPage.getRecords();
        Collections.reverse(msgRecords);
        return msgRecords;
    }

    /**
     * 查询双方聊天信息列表（首次登陆）
     */
    @Override
    public Map<String, List<QuickChatMsg>> getByAccountIds(List<String> accountIds, Integer current, Integer size) {
        // 构建通信双方关联key，封装List
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        Set<String> relationSet = new HashSet<>();
        for (String toAccountId : accountIds) {
            String relationId = RelationUtil.generate(loginAccountId, toAccountId);
            relationSet.add(relationId);
        }

        // 批量查询聊天信息
        Map<String, List<QuickChatMsg>> resultMap = new HashMap<>();
        for (String relationId : relationSet) {
            List<QuickChatMsg> msgList = this.getByRelationId(relationId, current, size);
            resultMap.put(relationId, msgList);
        }
        return resultMap;
    }
}
