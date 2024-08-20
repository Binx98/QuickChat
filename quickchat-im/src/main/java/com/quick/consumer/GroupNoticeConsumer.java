package com.quick.consumer;

import cn.hutool.json.JSONUtil;
import com.quick.constant.KafkaConstant;
import com.quick.enums.WsPushEnum;
import com.quick.netty.UserChannelRelation;
import com.quick.pojo.entity.WsPushEntity;
import com.quick.pojo.po.QuickChatApply;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.store.QuickChatGroupMemberStore;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-08-12  10:45
 * @Description: 群内通知消费者
 * @Version: 1.0
 */
@Component
public class GroupNoticeConsumer {
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    /**
     * 群内通知：添加群成员
     */
    @KafkaListener(topics = KafkaConstant.GROUP_ADD_MEMBER_NOTICE, groupId = KafkaConstant.CHAT_SEND_GROUP_ID)
    public void addGroupMember(String message) {
        QuickChatApply apply = JSONUtil.parse(message).toBean(QuickChatApply.class);
        List<QuickChatGroupMember> members = memberStore.getListByGroupId(apply.getGroupId());
        for (QuickChatGroupMember member : members) {
            Channel channel = UserChannelRelation.getUserChannelMap().get(member.getAccountId());
            if (ObjectUtils.isNotEmpty(channel)) {
                WsPushEntity<QuickChatApply> pushEntity = new WsPushEntity<>();
                pushEntity.setPushType(WsPushEnum.GROUP_ADD_MEMBER_NOTICE.getCode());
                pushEntity.setMessage(apply);
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
            }
        }
    }

    /**
     * 群内通知：删除群成员
     */
    @KafkaListener(topics = KafkaConstant.GROUP_DELETE_MEMBER_NOTICE, groupId = KafkaConstant.CHAT_SEND_GROUP_ID)
    public void deleteGroupMember(String message) {
        QuickChatGroupMember member = JSONUtil.parse(message).toBean(QuickChatGroupMember.class);
        Channel channel = UserChannelRelation.getUserChannelMap().get(member.getAccountId());
        if (ObjectUtils.isNotEmpty(channel)) {
            WsPushEntity<QuickChatGroupMember> pushEntity = new WsPushEntity<>();
            pushEntity.setPushType(WsPushEnum.GROUP_DELETE_MEMBER_NOTICE.getCode());
            pushEntity.setMessage(member);
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
        }
    }

    /**
     * 群内通知：解散群聊
     */
    @KafkaListener(topics = KafkaConstant.GROUP_RELEASE_NOTICE, groupId = KafkaConstant.CHAT_SEND_GROUP_ID)
    public void releaseGroup(String message) {
        Map<String, Object> params = JSONUtil.parse(message).toBean(Map.class);
        List<String> accountIds = (List<String>) params.get("accountIds");
        Long groupId = (Long) params.get("groupId");
        for (String accountId : accountIds) {
            Channel channel = UserChannelRelation.getUserChannelMap().get(accountId);
            if (ObjectUtils.isNotEmpty(channel)) {
                WsPushEntity<Long> pushEntity = new WsPushEntity<>();
                pushEntity.setPushType(WsPushEnum.GROUP_RELEASE_NOTICE.getCode());
                pushEntity.setMessage(groupId);
                channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(pushEntity)));
            }
        }
    }
}
