package com.quick.adapter;

import com.quick.pojo.QuickChatMsg;

import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 15:40
 * @Version 1.0
 * @Description: 聊天信息适配器
 */
public class ChatMsgAdapter {
    public static QuickChatMsg buildChatMsgPO(String accountId, String receiveId, String content, Integer type) {
        return QuickChatMsg.builder()
                .sendId(accountId)
                .receiveId(receiveId)
                .content(content)
                .type(type)
                .build();
    }

    public static void buildChatMsgVOList(List<QuickChatMsg> msgList) {
        
    }
}
