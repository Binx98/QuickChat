package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.QuickChatMsg;
import com.quick.store.QuickChatMsgStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 聊天信息 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatMsgStoreImpl extends ServiceImpl<QuickChatMsgMapper, QuickChatMsg> implements QuickChatMsgStore {
    /**
     * 保存聊天信息
     */
    @Override
    public Boolean saveMsg(QuickChatMsg chatMsg) {
        return this.save(chatMsg);
    }

    /**
     * 查询通讯双方聊天记录列表
     */
    @Override
    public List<QuickChatMsg> getChatMsg(String loginAccountId, String accountId) {
        return null;
    }
}
