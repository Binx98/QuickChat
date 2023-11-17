package com.quick.adapter;

import com.quick.pojo.po.QuickUser;
import com.quick.pojo.vo.UserVO;
import org.springframework.beans.BeanUtils;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:03
 * @Version 1.0
 * @Description: 用户适配器
 */
public class UserAdapter {
    public static UserVO buildUserVO(QuickUser userPO) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userPO, userVO);
        return userVO;
    }

    public static QuickUser buildUserPO(String accountId, String nickName, String password1, Integer gender, String email) {
        return QuickUser.builder()
                .accountId(accountId)
                .nickName(nickName)
                .password(password1)
                .gender(null)
                .build();
    }
}
