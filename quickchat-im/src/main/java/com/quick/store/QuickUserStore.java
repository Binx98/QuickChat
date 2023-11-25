package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
public interface QuickUserStore extends IService<QuickUser> {

    QuickUser getByAccountId(String accountId);

    Boolean saveUserInfo(com.quick.pojo.po.QuickUser userPO);

    List<QuickUser> getListByAccountIds(List<String> receiveIds);
}
