package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatFriendContact;
import com.quick.pojo.vo.ChatUserVO;

import java.util.List;

/**
 * <p>
 * 通讯录-好友 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
public interface QuickChatFriendContactService extends IService<QuickChatFriendContact> {

    /**
     * 查询通讯录好友
     *
     * @return 通讯录好友列表
     */
    List<ChatUserVO> getFriendList();

    /**
     * 添加好友
     *
     * @param toId      用户id
     * @param applyInfo 申请信息
     * @return 执行结果
     */
    Boolean addFriend(String toId, String applyInfo);

    /**
     * 删除好友
     *
     * @param toId 用户id
     * @return 执行结果
     */
    Boolean deleteFriend(String toId);
}
