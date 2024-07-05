package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatGroupContact;
import com.quick.pojo.vo.ChatUserVO;

import java.util.List;

/**
 * <p>
 * 通讯录-群聊 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-07-05
 */
public interface QuickChatGroupContactService extends IService<QuickChatGroupContact> {
    /**
     * 查询通讯录-群聊列表
     *
     * @return 通讯录-群聊列表
     */
    List<QuickChatGroupContact> getGroupContactList();
}
