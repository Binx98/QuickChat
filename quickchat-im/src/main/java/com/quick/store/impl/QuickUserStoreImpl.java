package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickUserMapper;
import com.quick.pojo.po.QuickUser;
import com.quick.store.QuickUserStore;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
@Service
public class QuickUserStoreImpl extends ServiceImpl<QuickUserMapper, QuickUser> implements QuickUserStore {
    /**
     * 根据 account_id 查询用户信息
     */
    @Override
    public QuickUser getByAccountId(String accountId) {
        return this.lambdaQuery()
                .eq(QuickUser::getAccountId, accountId)
                .one();
    }

    /**
     * 保存用户信息
     */
    @Override
    public Boolean saveUserInfo(QuickUser userPO) {
        return this.save(userPO);
    }
}
