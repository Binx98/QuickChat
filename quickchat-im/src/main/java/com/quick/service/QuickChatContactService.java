package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatContact;
import com.quick.pojo.vo.ChatUserVO;

import java.util.List;

/**
 * <p>
 * 通讯录 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
public interface QuickChatContactService extends IService<QuickChatContact> {

    /**
     * 查询通讯录好友
     *
     * @return 通讯录好友列表
     */
    List<ChatUserVO> getContactList();

    /**
     * 添加好友
     *
     * @param accountId 用户id
     * @param applyInfo 申请信息
     * @return 执行结果
     */
    void addFriend(String accountId, String applyInfo);

    /**
     * 删除好友
     *
     * @param accountId 用户id
     * @return 执行结果
     */
    void deleteFriend(String accountId);
}
