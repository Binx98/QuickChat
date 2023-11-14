package com.salt.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.salt.mapper.SaltUserMapper;
import com.salt.pojo.po.SaltUser;
import com.salt.store.SaltUserStore;
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
public class SaltUserStoreImpl extends ServiceImpl<SaltUserMapper, SaltUser> implements SaltUserStore {
    /**
     * 根据 account_id 查询用户信息
     */
    @Override
    public SaltUser getByAccountId(String accountId) {
        return this.lambdaQuery()
                .eq(SaltUser::getAccountId, accountId)
                .one();
    }

    /**
     * 保存用户信息
     */
    @Override
    public Boolean saveUserInfo(SaltUser userPO) {
        return this.save(userPO);
    }
}
