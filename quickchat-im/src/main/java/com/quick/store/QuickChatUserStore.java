package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatUser;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
public interface QuickChatUserStore extends IService<QuickChatUser> {
    /**
     * 根据 account_id 查询用户信息
     *
     * @param accountId 账户id
     * @return 用户信息
     */
    QuickChatUser getByAccountId(String accountId);

    /**
     * 保存用户信息
     *
     * @param userPO 用户信息
     * @return 执行结果
     */
    Boolean saveUser(QuickChatUser userPO);

    /**
     * 根据 account_id 列表查询用户列表
     *
     * @param accountIds account_id列表
     * @return 用户列表
     */
    List<QuickChatUser> getListByAccountIds(List<String> accountIds);

    /**
     * 修改用户信息
     *
     * @param userPO 用户信息
     * @return 执行结果
     */
    Boolean updateUserById(QuickChatUser userPO);

    /**
     * 根据 email 查询用户信息
     * @param email
     * @return
     */
    QuickChatUser getByEmail(String email);
}
