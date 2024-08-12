package com.quick.consumer;

import cn.hutool.json.JSONUtil;
import com.quick.constant.KafkaConstant;
import com.quick.enums.WsPushEnum;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.entity.WsPushEntity;
import com.quick.pojo.po.QuickChatGroupMember;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-08-12  10:45
 * @Description: 群内通知消费者
 * @Version: 1.0
 */
@Component
public class GroupNoticeConsumer {

    /**
     * 群内通知：删除群成员
     */
    @KafkaListener(topics = KafkaConstant.GROUP_DELETE_MEMBER_NOTICE, groupId = KafkaConstant.CHAT_SEND_GROUP_ID)
    public void sendFriendApply(String message) {
        QuickChatGroupMember member = JSONUtil.parse(message).toBean(QuickChatGroupMember.class);
        Channel channel = UserChannelRelation.getUserChannelMap().get(member.getAccountId());
        if (ObjectUtils.isNotEmpty(channel)) {
            WsPushEntity<QuickChatGroupMember> pushEntity = new WsPushEntity<>();
            pushEntity.setPushType(WsPushEnum.GROUP_DELETE_MEMBER_NOTICE.getCode());
            pushEntity.setMessage(member);
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
        }
    }
}
