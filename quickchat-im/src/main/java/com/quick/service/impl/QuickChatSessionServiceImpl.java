package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatSessionMapper;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.vo.ChatSessionVO;
import com.quick.service.QuickChatSessionService;
import com.quick.store.QuickChatSessionStore;
import com.quick.utils.RequestHolderUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 聊天会话（针对单聊） 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatSessionServiceImpl extends ServiceImpl<QuickChatSessionMapper, QuickChatSession> implements QuickChatSessionService {
    @Autowired
    private QuickChatSessionStore sessionStore;

    /**
     * 查询会话列表
     */
    @Override
    public List<ChatSessionVO> getSessionList() {
        // 获取当前登录用户账户id
        String accountId = (String) RequestHolderUtil.get().get("account_id");
        if (StringUtils.isEmpty(accountId)) {
            throw new QuickException(ResponseEnum.USER_NOT_EXIST);
        }

        // 查询会话列表
        List<QuickChatSession> sessionList = sessionStore.getListByAccountId(accountId);
        return null;
    }
}
