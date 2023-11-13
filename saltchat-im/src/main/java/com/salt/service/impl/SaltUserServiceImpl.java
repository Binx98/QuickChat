package com.salt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.salt.adapter.UserAdapter;
import com.salt.enums.ResponseEnum;
import com.salt.exception.SaltException;
import com.salt.mapper.SaltUserMapper;
import com.salt.pojo.po.SaltUser;
import com.salt.pojo.vo.UserVO;
import com.salt.service.SaltUserService;
import com.salt.store.SaltUserStore;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SaltUserServiceImpl extends ServiceImpl<SaltUserMapper, SaltUser> implements SaltUserService {
    @Autowired
    private SaltUserStore userStore;

    /**
     * 根据 account_id 查询用户信息
     */
    @Override
    public UserVO getByAccountId(String accountId) {
        SaltUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isEmpty(userPO)) {
            throw new SaltException(ResponseEnum.USER_NOT_EXIST);
        }
        return UserAdapter.buildUserVO(userPO);
    }
}
