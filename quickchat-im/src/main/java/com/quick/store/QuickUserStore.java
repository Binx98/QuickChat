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
public interface QuickUserStore extends IService<QuickChatUser> {

    QuickChatUser getByAccountId(String accountId);

    Boolean saveUser(QuickChatUser userPO);

    List<QuickChatUser> getListByAccountIds(List<String> accountIds);

    Boolean updateInfo(QuickChatUser userPO);
}
