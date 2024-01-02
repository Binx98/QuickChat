package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.dto.EmailDTO;
import com.quick.pojo.dto.LoginDTO;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.dto.UserUpdateDTO;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.UserVO;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
public interface QuickUserService extends IService<QuickChatUser> {

    UserVO getByAccountId(String accountId) throws Exception;

    Boolean register(RegisterDTO registerDTO) throws Exception;

    Map<String, Object> login(LoginDTO loginDTO) throws Exception;

    void captcha();

    Boolean sendEmail(EmailDTO emailDTO) throws Throwable;

    Boolean updateUser(UserUpdateDTO userDTO);

    QuickChatUser getByToken(String token);
}
