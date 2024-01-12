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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

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
    public Map<String, List<QuickChatMsg>> getByAccountIds(List<String> accountIds) throws ExecutionException, InterruptedException {
        // 遍历生成 relation_id
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        Set<String> relationIdSet = new HashSet<>();
        for (String toAccountId : accountIds) {
            String relationId = RelationUtil.generate(loginAccountId, toAccountId);
            relationIdSet.add(relationId);
        }

        // 分组：10个/组
        List<String> relationIds = relationIdSet.stream().collect(Collectors.toList());
        List<List<String>> relationIdList = ListUtil.fixedAssign(relationIds, 10);

        // 多线程异步查询聊天信息
        List<CompletableFuture<List<QuickChatMsg>>> futureList = new ArrayList<>();
        for (List<String> idList : relationIdList) {
            CompletableFuture<List<QuickChatMsg>> future = CompletableFuture.supplyAsync(
                    () -> msgStore.getByRelationIdList(idList), taskExecutor
            );
            futureList.add(future);
        }

        // 同步等待线程任务完毕，拿到聊天记录结果
        List<QuickChatMsg> msgResultList = new ArrayList<>();
        for (CompletableFuture<List<QuickChatMsg>> future : futureList) {
            List<QuickChatMsg> msgList = future.get();
            msgResultList.addAll(msgList);
        }

        // TODO 按照 relation_id 分组
        // TODO 封装VO
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
