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
        ChatUserVO userVO = new ChatUserVO();
        userVO.setAccountId(userPO.getAccountId());
        userVO.setNickName(userPO.getNickName());
        userVO.setGender(userPO.getGender());
        userVO.setLocation(userPO.getLocation());
        userVO.setCreateTime(userPO.getCreateTime());
        return userVO;
    }

    public static QuickChatUser buildUserPO(String accountId, String avatar, String password,
                                            String email, String location, String lineStatus) {
        QuickChatUser user = new QuickChatUser();
        user.setAccountId(accountId);
        user.setPassword(password);
        user.setAvatar(avatar);
        user.setEmail(email);
        user.setLocation(location);
        user.setLineStatus(lineStatus);
        return user;
    }

    public static QuickChatUser buildUserPO(UserUpdateDTO userDTO) {
        QuickChatUser user = new QuickChatUser();
        return user;
    }
}
