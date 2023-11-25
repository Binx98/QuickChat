package com.quick.adapter;

import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickUser;
import com.quick.pojo.vo.ChatSessionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 13:35
 * @Version 1.0
 * @Description: 用户会话适配器
 */
public class ChatSessionAdapter {
    public static List<ChatSessionVO> buildSessionVOList(List<QuickChatSession> sessionList, List<QuickUser> userList) {
        // 使用HashMap数据结构降低O(N^2)时间复杂度
        List<ChatSessionVO> resultList = new ArrayList<>();
        return resultList;
    }
}
