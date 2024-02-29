package com.quick.strategy.chatmsg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.constant.MQConstant;
import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ChatTypeEnum;
import com.quick.kafka.KafkaProducer;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import com.quick.utils.RedissonLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:07
 * @Description: 文件消息（图片、视频等）
 * @Version: 1.0
 */
@Component
public class FileHandler extends AbstractChatMsgStrategy {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.FILE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendChatMsg(QuickChatMsg chatMsg, QuickChatSession chatSession) throws Throwable {
        // 通过Channel推送消息（区分单聊、群聊）
        if (ChatTypeEnum.SINGLE.getType().equals(chatSession.getType())) {
            kafkaProducer.send(MQConstant.SEND_CHAT_SINGLE_MSG, JSONUtil.toJsonStr(chatMsg));
        } else {
            kafkaProducer.send(MQConstant.SEND_CHAT_GROUP_MSG, JSONUtil.toJsonStr(chatMsg));
        }
    }
}
