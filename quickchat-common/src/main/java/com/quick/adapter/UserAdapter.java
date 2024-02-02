package com.quick.adapter;

import com.quick.pojo.dto.UserUpdateDTO;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:03
 * @Version 1.0
 * @Description: 用户适配器
 */
public class UserAdapter {
    public static ChatUserVO buildUserVO(QuickChatUser userPO) {
        return ChatUserVO.builder()
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

    public static QuickChatUser buildUserPO(String accountId, String avatar, String password,
                                            String email, String location, String lineStatus) {
        return QuickChatUser.builder()
                .accountId(accountId)
                .password(password)
                .avatar(avatar)
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
