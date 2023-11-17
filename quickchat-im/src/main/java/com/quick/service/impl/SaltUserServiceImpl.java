package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.UserAdapter;
import com.quick.enums.ResponseEnum;
import com.quick.exception.SaltException;
import com.quick.mapper.SaltUserMapper;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.po.SaltUser;
import com.quick.pojo.vo.UserVO;
import com.quick.service.SaltUserService;
import com.quick.store.SaltUserStore;
//import com.salt.util.RedisUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
@Service
public class SaltUserServiceImpl extends ServiceImpl<SaltUserMapper, SaltUser> implements SaltUserService {
    @Autowired
    private SaltUserStore userStore;

    /**
     * 根据 account_id 查询用户信息
     */
    @Override
    public UserVO getByAccountId(String accountId) {
        SaltUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isEmpty(userPO)) {
            throw new SaltException(ResponseEnum.USER_NOT_EXIST);
        }
        return UserAdapter.buildUserVO(userPO);
    }

    /**
     * 注册账号
     */
    @Override
    public Boolean register(RegisterDTO registerDTO) {
        String accountId = registerDTO.getAccountId();
        String nickName = registerDTO.getNickName();
        String password1 = registerDTO.getPassword1();
        String password2 = registerDTO.getPassword2();
        Integer gender = registerDTO.getGender();
        String email = registerDTO.getEmail();
        String captchaCode = registerDTO.getCaptchaCode();

        // 两次密码输入是否一致
        if (!password1.equals(password2)) {
            throw new SaltException(ResponseEnum.PASSWORD_DIFF);
        }

        // TODO 图片验证码信息是否输入正确
//        if (captchaCode.equalsIgnoreCase()) {
//
//        }

        // 判断账号是否存在
        SaltUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isNotEmpty(userPO)) {
            throw new SaltException(ResponseEnum.ACCOUNT_ID_EXIST);
        }

        // 保存账号信息
        userPO = UserAdapter.buildUserPO(accountId, nickName, password1, gender, email);
        return userStore.saveUserInfo(userPO);
    }
}
