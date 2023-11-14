package com.salt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.salt.pojo.dto.RegisterDTO;
import com.salt.pojo.po.SaltUser;
import com.salt.pojo.vo.UserVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
public interface SaltUserService extends IService<SaltUser> {

    UserVO getByAccountId(String accountId) throws Exception;

    Boolean register(RegisterDTO registerDTO);
}
