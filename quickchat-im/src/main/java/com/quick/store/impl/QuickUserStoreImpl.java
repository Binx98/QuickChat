package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickUserMapper;
import com.quick.pojo.po.QuickUser;
import com.quick.store.QuickUserStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Cacheable(value = RedisConstant.QUICK_USER, key = "#p0")
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

    /**
     * 批量查询用户信息
     */
    @Override
    public List<QuickUser> getListByAccountIds(List<String> receiveIds) {
        return this.lambdaQuery()
                .in(QuickUser::getAccountId, receiveIds)
                .list();
    }

    /**
     * 更新用户信息
     */
    @Override
    @CacheEvict(value = RedisConstant.QUICK_USER, key = "#p0.accountId")
    public Boolean updateInfo(QuickUser userPO) {
        return this.updateById(userPO);
    }
}
