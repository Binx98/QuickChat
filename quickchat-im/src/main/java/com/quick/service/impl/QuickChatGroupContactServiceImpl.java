package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatGroupContactMapper;
import com.quick.pojo.po.QuickChatGroupContact;
import com.quick.service.QuickChatGroupContactService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 通讯录-群聊 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Service
public class QuickChatGroupContactServiceImpl extends ServiceImpl<QuickChatGroupContactMapper, QuickChatGroupContact> implements QuickChatGroupContactService {
    @Override
    public List<QuickChatGroupContact> getGroupContactList() {
        return null;
    }
}
