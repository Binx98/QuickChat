package com.quick.store.doris.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.doris.QuickChatMsgDorisStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 聊天信息 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-11-06
 */
@Service
@DS("doris")
public class QuickChatMsgDorisStoreImpl extends ServiceImpl<QuickChatMsgMapper, QuickChatMsg> implements QuickChatMsgDorisStore {
    @Override
    public Boolean saveBatchMsg(List<QuickChatMsg> msgList) {
        return this.saveBatch(msgList);
    }

    @Override
    public Page<QuickChatMsg> getHisPageByRelationId(Long relationId, Integer current, Integer size) {
        return null;
    }
}
