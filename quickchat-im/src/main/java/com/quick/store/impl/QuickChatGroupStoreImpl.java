package com.quick.store.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.mapper.QuickChatGroupMapper;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.store.QuickChatGroupStore;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群聊 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
@Service
public class QuickChatGroupStoreImpl extends ServiceImpl<QuickChatGroupMapper, QuickChatGroup> implements QuickChatGroupStore {

}
