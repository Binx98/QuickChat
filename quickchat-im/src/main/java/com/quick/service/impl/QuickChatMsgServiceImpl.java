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
import java.util.stream.Collectors;

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
    public Map<String, List<QuickChatMsg>> getByAccountIds(List<String> accountIds) {
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);

        // 遍历生成 relation_id（去重）
        Set<String> relationIdSet = new HashSet<>();
        for (String toAccountId : accountIds) {
            String relationId = RelationUtil.generate(loginAccountId, toAccountId);
            relationIdSet.add(relationId);
        }

        // 批量查询聊天记录 TODO 转成VO
        List<String> relationIds = relationIdSet.stream().collect(Collectors.toList());
        List<QuickChatMsg> msgList = msgStore.getByRelationIdList(relationIds);

        Map<String, List<QuickChatMsg>> resultMap = msgList.stream().collect(Collectors.groupingBy(QuickChatMsg::getToId));
        return resultMap;
    }
}
