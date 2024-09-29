package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.dto.*;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;

import java.io.IOException;
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
    /**
     * 根据 account_id 查询用户信息
     *
     * @param accountId 账户id
     * @return 用户信息VO
     * @throws Exception
     */
    ChatUserVO getByAccountId(String accountId) throws Exception;

    /**
     * 账号注册
     *
     * @param registerDTO 注册表单DTO
     * @return 执行结果
     * @throws Exception
     */
    void register(RegisterFormDTO registerDTO) throws Exception;

    /**
     * 登录功能
     *
     * @param loginDTO 登录表单DTO
     * @return Token和账户信息
     * @throws Exception
     */
    Map<String, Object> login(LoginFormDTO loginDTO) throws Exception;

    /**
     * 图片验证码
     *
     * @throws IOException
     */
    void captcha() throws IOException;

    /**
     * 发送邮件
     *
     * @param emailDTO 邮件入参DTO
     * @return 执行结果
     * @throws Throwable
     */
    void sendEmail(EmailDTO emailDTO) throws Throwable;

    /**
     * 修改用户信息
     *
     * @param userDTO 修改表单DTO
     * @return 执行结果
     */
    void updateUser(UserInfoDTO userDTO);

    /**
     * 根据请求头Token解析用户信息
     *
     * @return 用户信息PO
     */
    QuickChatUser getByToken();

    /**
     * 用户找回密码
     *
     * @param findBackDTO 找回密码DTO
     * @return 执行结果
     * @throws Exception
     */
    void findBack(FindBackFormDTO findBackDTO) throws Exception;

}
