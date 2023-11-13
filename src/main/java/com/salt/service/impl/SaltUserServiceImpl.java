package com.salt.service.impl;

import com.salt.pojo.SaltUser;
import com.salt.mapper.SaltUserMapper;
import com.salt.service.SaltUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
@Service
public class SaltUserServiceImpl extends ServiceImpl<SaltUserMapper, SaltUser> implements SaltUserService {

}
