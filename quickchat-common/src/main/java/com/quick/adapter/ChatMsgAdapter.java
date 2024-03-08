package com.quick.adapter;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.enums.YesNoEnum;
import com.quick.pojo.dto.ExtraInfoDTO;
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
                .leftVisible(YesNoEnum.NO.getStatus())
                .rightVisible(YesNoEnum.NO.getStatus())
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
                    .leftVisible(chatMsg.getLeftVisible())
                    .rightVisible(chatMsg.getRightVisible())
                    .createTime(chatMsg.getCreateTime())
                    .build();
            if (ObjectUtils.isNotEmpty(chatMsg.getExtraInfo())) {
                msgVO.setExtraInfo(JSONUtil.toBean(chatMsg.getExtraInfo(), ExtraInfoDTO.class));
            }
            resultList.add(msgVO);
        }
        return resultList;
    }
}
