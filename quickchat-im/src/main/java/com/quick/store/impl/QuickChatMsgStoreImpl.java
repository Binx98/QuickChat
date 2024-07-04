package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatMsgStore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 聊天信息 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatMsgStoreImpl extends ServiceImpl<QuickChatMsgMapper, QuickChatMsg> implements QuickChatMsgStore {
    @Override
    public Boolean saveMsg(QuickChatMsg chatMsg) {
        return this.save(chatMsg);
    }

    @Override
    public Page<QuickChatMsg> getByRelationId(Long relationId, Integer current, Integer size) {
        return this.lambdaQuery()
                .eq(QuickChatMsg::getRelationId, relationId)
                .orderByDesc(QuickChatMsg::getCreateTime)
                .page(new Page<>(current, size));
    }

    @Override
    public List<QuickChatMsg> getByRelationIdList(List<Long> relationIds) {
        return this.lambdaQuery()
                .in(QuickChatMsg::getRelationId, relationIds)
                .orderByDesc(QuickChatMsg::getCreateTime)
                .last(" LIMIT 30 ")
                .list();
    }

    @Override
    public QuickChatMsg getByMsgId(Long msgId) {
        return this.lambdaQuery()
                .eq(QuickChatMsg::getId, msgId)
                .one();
    }

    @Override
    public Boolean updateByMsgId(QuickChatMsg chatMsg) {
        return this.updateById(chatMsg);
    }


    @Override
    public Integer getUnreadCount(String loginAccountId, Long relationId, LocalDateTime lastReadTime) {
        return this.lambdaQuery()
                .eq(QuickChatMsg::getRelationId, relationId)
                .ne(QuickChatMsg::getFromId, loginAccountId)
                .gt(QuickChatMsg::getCreateTime, lastReadTime)
                .groupBy(QuickChatMsg::getRelationId)
                .count();
    }
}
