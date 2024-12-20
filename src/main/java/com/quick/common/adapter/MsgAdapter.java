package com.quick.common.adapter;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.common.enums.ChatMsgEnum;
import com.quick.common.pojo.dto.FileExtraDTO;
import com.quick.common.pojo.po.QuickChatMsg;
import com.quick.common.pojo.vo.ChatMsgVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 15:40
 * @Version 1.0
 * @Description: 聊天信息适配器
 */
public class MsgAdapter {
    public static QuickChatMsg buildChatMsgPO(String fromId, String toId, Long relationId,
                                              String nickName, String content, Long quoteId,
                                              String extraInfo, Integer type) {
        QuickChatMsg chatMsg = new QuickChatMsg();
        chatMsg.setFromId(fromId);
        chatMsg.setToId(toId);
        chatMsg.setRelationId(relationId);
        chatMsg.setNickName(nickName);
        chatMsg.setContent(content);
        chatMsg.setMsgType(type);
        chatMsg.setExtraInfo(extraInfo);
        chatMsg.setQuoteId(quoteId);
        return chatMsg;
    }

    public static List<ChatMsgVO> buildChatMsgVOList(List<QuickChatMsg> msgList) {
        List<ChatMsgVO> resultList = new ArrayList<>();
        for (QuickChatMsg chatMsg : msgList) {
            // 封装消息展示VO
            ChatMsgVO msgVO = ChatMsgVO.builder()
                    .accountId(chatMsg.getFromId())
                    .content(chatMsg.getContent())
                    .relationId(chatMsg.getRelationId())
                    .nickName(chatMsg.getNickName())
                    .msgType(chatMsg.getMsgType())
                    .createTime(chatMsg.getCreateTime())
                    .build();

            // 文件消息：封装文件额外信息
            if (ObjectUtils.isNotEmpty(chatMsg.getExtraInfo())) {
                msgVO.setExtraInfo(JSONUtil.toBean(chatMsg.getExtraInfo(), FileExtraDTO.class));
            }

            // 撤回消息：隐藏敏感数据
            if (ChatMsgEnum.RECALL.getCode().equals(chatMsg.getMsgType())) {
                msgVO.setContent(null);
                msgVO.setExtraInfo(null);
            }
            resultList.add(msgVO);
        }
        return resultList;
    }
}
