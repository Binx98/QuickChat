package com.quick.consumer.notice;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.constant.RocketMQConstant;
import com.quick.enums.WsPushEnum;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.entity.WsPushEntity;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.store.QuickChatGroupMemberStore;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2024/10/6 14:42
 * @Version 1.0
 * @Description: 移除群成员-消费者
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.GROUP_DELETE_MEMBER_NOTICE, consumerGroup = RocketMQConstant.CHAT_SEND_GROUP_ID)
public class GroupDeleteMemberConsumer implements RocketMQListener<String> {
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    public void onMessage(String message) {
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
