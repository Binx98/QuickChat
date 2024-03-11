package com.quick.adapter;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.enums.ChatMsgEnum;
import com.quick.pojo.dto.FileExtraDTO;
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
    public static QuickChatMsg buildChatMsgPO(String fromId, String toId, String content, String extraInfo, Integer type) {
        return QuickChatMsg.builder()
                .fromId(fromId)
                .toId(toId)
                .relationId(RelationUtil.generate(fromId, toId))
                .content(content)
                .extraInfo(extraInfo)
                .msgType(type)
                .build();
    }

    public static List<ChatMsgVO> buildChatMsgVOList(List<QuickChatMsg> msgList) {
        List<ChatMsgVO> resultList = new ArrayList<>();
        for (QuickChatMsg chatMsg : msgList) {
            // 封装消息展示VO
            ChatMsgVO msgVO = ChatMsgVO.builder()
                    .accountId(chatMsg.getFromId())
                    .content(chatMsg.getContent())
                    .relationId(chatMsg.getRelationId())
                    .msgType(chatMsg.getMsgType())
                    .createTime(chatMsg.getCreateTime())
                    .build();
            // 文件消息：封装文件额外信息
            if (ObjectUtils.isNotEmpty(chatMsg.getExtraInfo())) {
                msgVO.setExtraInfo(JSONUtil.toBean(chatMsg.getExtraInfo(), FileExtraDTO.class));
            }
            // 撤回消息：隐藏敏感数据
            if (ChatMsgEnum.RECALL.getType().equals(chatMsg.getMsgType())) {
                msgVO.setContent(null);
                msgVO.setMsgType(null);
                msgVO.setExtraInfo(null);
                msgVO.setCreateTime(null);
            }
            resultList.add(msgVO);
        }
        return resultList;
    }
}
