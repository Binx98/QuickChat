package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatApplyMapper;
import com.quick.pojo.po.QuickChatApply;
import com.quick.store.QuickChatApplyStore;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统通知 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-03-12
 */
@Service
public class QuickChatApplyStoreImpl extends ServiceImpl<QuickChatApplyMapper, QuickChatApply> implements QuickChatApplyStore {

    @Override
    public Boolean saveInfo(QuickChatApply apply) {
        return this.save(apply);
    }
}
