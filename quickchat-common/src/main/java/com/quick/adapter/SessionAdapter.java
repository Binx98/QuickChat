package com.quick.adapter;

import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatSessionVO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 13:35
 * @Version 1.0
 * @Description: 聊天会话适配器
 */
public class SessionAdapter {
    public static List<ChatSessionVO> buildSessionVOList(List<QuickChatSession> sessionList,
                                                         List<QuickChatUser> userList,
                                                         List<QuickChatGroup> groupList) {
        // 使用 Map 数据结构降低O(N^2)时间复杂度
        Map<String, ChatSessionVO> map = new HashMap<>();
        List<ChatSessionVO> resultList = new ArrayList<>();

        // 遍历会话列表，封装到 Map(to_id, sessionVO)
        for (QuickChatSession chatSession : sessionList) {
            ChatSessionVO sessionVO = new ChatSessionVO();
            sessionVO.setSessionId(chatSession.getId());
            sessionVO.setToId(chatSession.getToId());
            sessionVO.setFromId(chatSession.getFromId());
            sessionVO.setType(chatSession.getType());
            sessionVO.setRelationId(chatSession.getRelationId());
            sessionVO.setUpdateTime(chatSession.getUpdateTime());
            sessionVO.setLastReadTime(chatSession.getLastReadTime());
            map.put(chatSession.getToId(), sessionVO);
        }

        // 遍历用户列表
        for (QuickChatUser user : userList) {
            if (map.containsKey(user.getAccountId())) {
                ChatSessionVO sessionVO = map.get(user.getAccountId());
                sessionVO.setSessionName(user.getNickName());
                sessionVO.setAvatar(user.getAvatar());
                sessionVO.setGender(user.getGender());
                sessionVO.setLoginStatus(user.getLoginStatus());
                resultList.add(sessionVO);
            }
        }

        // 遍历群聊列表
        // 注意：发送给群聊消息 relation_id 是 group_id，所以群聊会话 relation_id 返回给前端只给 group_id（否则群聊消息无法展示）
        for (QuickChatGroup group : groupList) {
            if (map.containsKey(group.getId().toString())) {
                ChatSessionVO sessionVO = map.get(group.getId().toString());
                sessionVO.setSessionName(group.getGroupName());
                sessionVO.setAvatar(group.getGroupAvatar());
                sessionVO.setRelationId(group.getId());
                resultList.add(sessionVO);
            }
        }

        // 根据修改时间倒排（用于展示）
        resultList = resultList.stream()
                .sorted(Comparator.comparing(ChatSessionVO::getUpdateTime).reversed())
                .collect(Collectors.toList());
        return resultList;
    }

    public static QuickChatSession buildSessionPO(String fromId, String toId, Long relationId, Integer type) {
        return QuickChatSession.builder()
                .fromId(fromId)
                .toId(toId)
                .relationId(relationId)
                .type(type)
                .lastReadTime(LocalDateTime.now())
                .build();
    }

    public static QuickChatSession buildSessionPO(Long sessionId, LocalDateTime time) {
        return QuickChatSession.builder()
                .id(sessionId)
                .lastReadTime(time)
                .build();
    }

    public static ChatSessionVO buildUserSessionPO(QuickChatUser userPO, QuickChatSession sessionPO) {
        return ChatSessionVO.builder()
                .sessionId(sessionPO.getId())
                .fromId(sessionPO.getFromId())
                .toId(sessionPO.getToId())
                .relationId(sessionPO.getRelationId())
                .updateTime(sessionPO.getUpdateTime())
                .lastReadTime(sessionPO.getLastReadTime())
                .sessionName(userPO.getNickName())
                .avatar(userPO.getAvatar())
                .gender(userPO.getGender())
                .loginStatus(userPO.getLoginStatus())
                .build();
    }

    public static ChatSessionVO buildGroupSessionPO(QuickChatGroup groupPO, QuickChatSession sessionPO) {
        return ChatSessionVO.builder()
                .sessionId(sessionPO.getId())
                .fromId(sessionPO.getFromId())
                .toId(sessionPO.getToId())
                .relationId(sessionPO.getRelationId())
                .updateTime(sessionPO.getUpdateTime())
                .lastReadTime(sessionPO.getLastReadTime())
                .sessionName(groupPO.getGroupName())
                .avatar(groupPO.getGroupAvatar())
                .build();
    }
}
