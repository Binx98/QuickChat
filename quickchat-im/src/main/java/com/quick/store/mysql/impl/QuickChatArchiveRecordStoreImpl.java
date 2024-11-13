package com.quick.store.mysql.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.constant.RedisConstant;
import com.quick.mapper.QuickChatArchiveRecordMapper;
import com.quick.mapper.QuickChatUserMapper;
import com.quick.pojo.po.QuickChatArchiveRecord;
import com.quick.pojo.po.QuickChatUser;
import com.quick.store.mysql.QuickChatArchiveRecordStore;
import com.quick.store.mysql.QuickChatUserStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-11-13
 */
@Service
@DS("mysql")
public class QuickChatArchiveRecordStoreImpl extends ServiceImpl<QuickChatArchiveRecordMapper, QuickChatArchiveRecord> implements QuickChatArchiveRecordStore {

}
