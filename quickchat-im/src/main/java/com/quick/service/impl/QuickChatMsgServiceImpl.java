package com.quick.service.impl;

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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void getChatMsg(String accountId) {
        String loginAccountId = (String) RequestContextUtil.get().get("account_id");
        String relationId = RelationUtil.generate(loginAccountId, accountId);
        List<QuickChatMsg> msgList = msgStore.getMsgByRelationId(relationId);
        ChatMsgAdapter.buildChatMsgVOList(msgList);
    }

    /**
     * 查询双方聊天信息列表
     */
    @Override
    public Map<String, List<QuickChatMsg>> getMapByAccountIds(String loginAccountId, List<String> sessionAccountIds) {
        // 构建通信双方关联key，封装List
        Set<String> relationSet = new HashSet<>();
        for (String sessionAccountId : sessionAccountIds) {
            String relationId = RelationUtil.generate(loginAccountId, sessionAccountId);
            relationSet.add(relationId);
        }

        // 批量查询聊天信息


        return null;
    }
}
