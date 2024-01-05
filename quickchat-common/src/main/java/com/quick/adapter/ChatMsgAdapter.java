package com.quick.adapter;

import com.quick.pojo.po.QuickChatMsg;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 15:40
 * @Version 1.0
 * @Description: 聊天信息适配器
 */
public class ChatMsgAdapter {
    public static QuickChatMsg buildChatMsgPO(String fromId, String toId, String content, Integer type) {
        return QuickChatMsg.builder()
                .fromId(fromId)
                .toId(toId)
                .content(content)
                .msgType(type)
                .build();
    }
}
