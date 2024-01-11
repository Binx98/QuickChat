package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.service.QuickChatMsgService;
import com.quick.store.QuickChatMsgStore;
import com.quick.utils.ListUtil;
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
        // 遍历生成 relation_id（去重）
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        Set<String> relationIdSet = new HashSet<>();
        for (String toAccountId : accountIds) {
            String relationId = RelationUtil.generate(loginAccountId, toAccountId);
            relationIdSet.add(relationId);
        }

        // 平均10个一组
        List<String> relationIds = relationIdSet.stream().collect(Collectors.toList());
        List<List<String>> relationIdList = ListUtil.fixedAssign(relationIds, 10);

        // 循环查询
        for (List<String> idList : relationIdList) {
            List<QuickChatMsg> msgList = msgStore.getByRelationIdList(idList);

        }

//        Map<String, List<QuickChatMsg>> resultMap = msgList.stream().collect(Collectors.groupingBy(QuickChatMsg::getToId));
        return null;
    }

    /**
     * 将一组List数据平均分成n组
     *
     * @param source 要分组的数据源
     * @param n      平均分成n组
     */
    private <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<>();
        int remainder = source.size() % n;
        int number = source.size() / n;
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
}
