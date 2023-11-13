package com.salt.adapter;

import com.salt.pojo.po.SaltUser;
import com.salt.pojo.vo.UserVO;
import org.springframework.beans.BeanUtils;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:03
 * @Version 1.0
 * @Description: 用户适配器
 */
public class UserAdapter {
    public static UserVO buildUserVO(SaltUser userPO) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userPO, userVO);
        return userVO;
    }
}
