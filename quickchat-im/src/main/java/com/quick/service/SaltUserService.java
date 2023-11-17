package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.po.SaltUser;
import com.quick.pojo.vo.UserVO;

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
