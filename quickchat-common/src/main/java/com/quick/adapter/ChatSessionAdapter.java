package com.quick.adapter;

import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatSessionVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 13:35
 * @Version 1.0
 * @Description: 用户会话适配器
 */
public class ChatSessionAdapter {
    public static List<ChatSessionVO> buildSessionVOList(List<QuickChatSession> sessionList, List<QuickChatUser> userList) {
        // 使用HashMap数据结构降低O(N^2)时间复杂度
        Map<String, ChatSessionVO> map = new HashMap<>();
        List<ChatSessionVO> resultList = new ArrayList<>();

        // 遍历会话列表
        for (QuickChatSession chatSession : sessionList) {
            ChatSessionVO sessionVO = new ChatSessionVO();
            sessionVO.setAccountId(chatSession.getToId());
            map.put(chatSession.getToId(), sessionVO);
        }

        // 遍历用户列表
        for (QuickChatUser user : userList) {
            String accountId = user.getAccountId();
            if (map.containsKey(accountId)) {
                ChatSessionVO sessionVO = map.get(accountId);
                sessionVO.setNickName(user.getNickName());
                sessionVO.setAvatar(user.getAvatar());
                sessionVO.setGender(user.getGender());
                sessionVO.setEmail(user.getEmail());
                sessionVO.setLineStatus(user.getLineStatus());
                resultList.add(sessionVO);
            }
        }
        return resultList;
    }

    public static QuickChatSession buildSessionPO(String fromId, String toId) {
        return QuickChatSession.builder()
                .fromId(fromId)
                .toId(toId)
                .build();
    }
}