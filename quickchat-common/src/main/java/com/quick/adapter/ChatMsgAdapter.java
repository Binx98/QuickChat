package com.quick.adapter;

import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.vo.ChatMsgVO;
import com.quick.utils.RelationUtil;

import java.util.ArrayList;
import java.util.List;

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
                .relationId(RelationUtil.generate(fromId, toId))
                .content(content)
                .msgType(type)
                .build();
    }

    public static List<ChatMsgVO> buildChatMsgVOList(List<QuickChatMsg> msgList) {
        List<ChatMsgVO> resultList = new ArrayList<>();
        for (QuickChatMsg chatMsg : msgList) {
            ChatMsgVO msgVO = ChatMsgVO.builder()
                    .accountId(chatMsg.getFromId())
                    .content(chatMsg.getContent())
                    .relationId(chatMsg.getRelationId())
                    .msgType(chatMsg.getMsgType())
                    .createTime(chatMsg.getCreateTime())
                    .build();
            resultList.add(msgVO);
        }
        return resultList;
    }
}
