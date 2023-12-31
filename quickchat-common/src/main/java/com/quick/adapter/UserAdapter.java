package com.quick.adapter;

import com.quick.pojo.dto.UserUpdateDTO;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.UserVO;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:03
 * @Version 1.0
 * @Description: 用户适配器
 */
public class UserAdapter {
    public static UserVO buildUserVO(QuickChatUser userPO) {
        return UserVO.builder()
                .accountId(userPO.getAccountId())
                .nickName(userPO.getNickName())
                .gender(userPO.getGender())
                .location(userPO.getLocation())
                .createTime(userPO.getCreateTime())
                .build();
    }

    public static QuickChatUser buildUserPO(String accountId, String lineStatus) {
        return QuickChatUser.builder()
                .accountId(accountId)
                .lineStatus(lineStatus)
                .build();
    }

    public static QuickChatUser buildUserPO(String accountId, String password1,
                                            String email, String location, String lineStatus) {
        return QuickChatUser.builder()
                .accountId(accountId)
                .nickName(accountId)
                .password(password1)
                .email(email)
                .location(location)
                .lineStatus(lineStatus)
                .build();
    }

    public static QuickChatUser buildUserPO(UserUpdateDTO userDTO) {
        return QuickChatUser.builder()
                .build();
    }
}
