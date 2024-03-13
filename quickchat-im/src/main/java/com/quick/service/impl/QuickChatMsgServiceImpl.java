package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatFriend;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.vo.ChatMsgVO;
import com.quick.service.QuickChatFriendService;
import com.quick.service.QuickChatMsgService;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import com.quick.strategy.msg.ChatMsgStrategyFactory;
import com.quick.utils.ListUtil;
import com.quick.utils.RelationUtil;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    private QuickChatFriendService friendService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public Map<String, List<ChatMsgVO>> getByRelationId(String relationId, Integer current, Integer size) {
        Page<QuickChatMsg> msgPage = msgStore.getByRelationId(relationId, current, size);
        List<ChatMsgVO> chatMsgVOList = ChatMsgAdapter.buildChatMsgVOList(msgPage.getRecords());
        return chatMsgVOList.stream()
                .sorted(Comparator.comparing(ChatMsgVO::getCreateTime))
                .collect(Collectors.groupingBy(ChatMsgVO::getRelationId));
    }

    @Override
    public Map<String, List<ChatMsgVO>> getByAccountIds(List<String> accountIds) throws ExecutionException, InterruptedException {
        // 遍历生成 relation_id
        List<String> relationIds = new ArrayList<>();
        String loginAccountId = (String) RequestContextUtil.get().get(RequestContextUtil.ACCOUNT_ID);
        for (String toAccountId : accountIds) {
            relationIds.add(RelationUtil.generate(loginAccountId, toAccountId));
        }

        // 分组：10个/组，多线程异步查询聊天信息
        List<List<String>> relationIdList = ListUtil.fixedAssign(relationIds, 10);
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
            msgResultList.addAll(future.get());
        }

        // 转换成VO、按照 relation_id 分组
        List<ChatMsgVO> chatMsgVOList = ChatMsgAdapter.buildChatMsgVOList(msgResultList);
        Map<String, List<ChatMsgVO>> resultMap = chatMsgVOList.stream()
                .sorted(Comparator.comparing(ChatMsgVO::getCreateTime))
                .collect(Collectors.groupingBy(ChatMsgVO::getRelationId));

        // 没有 relation_id，空列表占位（首次发送消息需要占位）
        for (String relationId : relationIds) {
            if (!resultMap.containsKey(relationId)) {
                resultMap.put(relationId, new ArrayList<>());
            }
        }
        return resultMap;
    }

    @Override
    public void sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 判断对方是否是您的好友
        QuickChatFriend friendPO = friendService.getByFromIdAndToId(msgDTO.getFromId(), msgDTO.getToId());
        AbstractChatMsgStrategy chatMsgHandler = ChatMsgStrategyFactory.getStrategyHandler(msgDTO.getMsgType());
        chatMsgHandler.sendChatMsg(msgDTO);
    }
}
