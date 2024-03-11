package com.quick.strategy.msg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.constant.MQConstant;
import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ChatTypeEnum;
import com.quick.kafka.KafkaProducer;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.dto.FileExtraDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import com.quick.utils.RedissonLockUtil;
import com.quick.utils.RelationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:07
 * @Description: 文件消息（图片、视频等）
 * @Version: 1.0
 */
@Component
public class FileMsgHandler extends AbstractChatMsgStrategy {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private RedissonLockUtil lockUtil;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.FILE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendChatMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 保存聊天信息
        String fromId = msgDTO.getFromId();
        String toId = msgDTO.getToId();
        String fileUrl = msgDTO.getContent();
        FileExtraDTO extraInfo = msgDTO.getExtraInfo();
        QuickChatMsg chatMsg = ChatMsgAdapter.buildChatMsgPO(fromId, toId,
                fileUrl, JSONUtil.toJsonStr(extraInfo), this.getEnum().getCode());
        msgStore.saveMsg(chatMsg);

        // 处理双方会话信息
        String relationId = RelationUtil.generate(fromId, toId);
        QuickChatSession chatSession = lockUtil.executeWithLock(relationId, 15, TimeUnit.SECONDS,
                () -> this.handleSession(fromId, toId)
        );

        // 通过 Channel 推送消息（单聊、群聊）
        if (ChatTypeEnum.SINGLE.getType().equals(chatSession.getType())) {
            kafkaProducer.send(MQConstant.SEND_CHAT_SINGLE_MSG, JSONUtil.toJsonStr(chatMsg));
        }
        if (ChatTypeEnum.GROUP.getType().equals(chatSession.getType())) {
            kafkaProducer.send(MQConstant.SEND_CHAT_GROUP_MSG, JSONUtil.toJsonStr(chatMsg));
        }
    }
}
