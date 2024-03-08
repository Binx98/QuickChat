package com.quick.strategy.msg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.constant.MQConstant;
import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ChatTypeEnum;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.kafka.KafkaProducer;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import com.quick.utils.RedissonLockUtil;
import com.quick.utils.RelationUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-07  16:30
 * @Description: 撤回消息
 * @Version: 1.0
 */
@Component
public class RecallHandler extends AbstractChatMsgStrategy {
    @Autowired
    private RedissonLockUtil lockUtil;
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.RECALL;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendChatMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 超过2分钟消息不可撤回
        QuickChatMsg chatMsg = msgStore.getByMsgId(msgDTO.getMsgId());
        if (ObjectUtils.isEmpty(chatMsg)) {
            throw new QuickException(ResponseEnum.FAIL);
        }
        if (chatMsg.getCreateTime().minusMinutes(2).isBefore(LocalDateTime.now())) {
            throw new QuickException(ResponseEnum.CAN_NOT_RECALL);
        }

        // 消息类型修改为撤回
        chatMsg.setMsgType(this.getEnum().getType());
        msgStore.updateByMsgId(chatMsg);

        // 处理双方会话信息
        String relationId = RelationUtil.generate(msgDTO.getFromId(), msgDTO.getToId());
        QuickChatSession chatSession = lockUtil.executeWithLock(relationId, 15, TimeUnit.SECONDS,
                () -> this.handleSession(msgDTO.getFromId(), msgDTO.getToId())
        );

        // 通过Channel推送消息（单聊、群聊）
        if (ChatTypeEnum.SINGLE.getType().equals(chatSession.getType())) {
            kafkaProducer.send(MQConstant.SEND_CHAT_SINGLE_MSG, JSONUtil.toJsonStr(chatMsg));
        }
        if (ChatTypeEnum.GROUP.getType().equals(chatSession.getType())) {
            kafkaProducer.send(MQConstant.SEND_CHAT_GROUP_MSG, JSONUtil.toJsonStr(chatMsg));
        }
    }
}
