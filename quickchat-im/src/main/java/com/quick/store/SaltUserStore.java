package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.SaltUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
public interface SaltUserStore extends IService<SaltUser> {

    SaltUser getByAccountId(String accountId);

    Boolean saveUserInfo(com.quick.pojo.po.SaltUser userPO);
}
