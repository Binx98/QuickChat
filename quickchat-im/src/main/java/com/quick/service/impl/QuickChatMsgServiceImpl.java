package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.QuickChatMsg;
import com.quick.service.QuickChatMsgService;
import com.quick.store.QuickChatMsgStore;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 聊天信息 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Service
public class QuickChatMsgServiceImpl extends ServiceImpl<QuickChatMsgMapper, QuickChatMsg> implements QuickChatMsgService {

}
