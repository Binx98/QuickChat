package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ContactAdapter;
import com.quick.adapter.GroupMemberAdapter;
import com.quick.adapter.SessionAdapter;
import com.quick.constant.RocketMQConstant;
import com.quick.enums.ResponseEnum;
import com.quick.enums.SessionTypeEnum;
import com.quick.enums.YesNoEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatApplyMapper;
import com.quick.pojo.po.QuickChatApply;
import com.quick.pojo.po.QuickChatContact;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatSession;
import com.quick.service.QuickChatApplyService;
import com.quick.store.QuickChatApplyStore;
import com.quick.store.QuickChatContactStore;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.utils.RequestContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 申请通知 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-03-12
 */
@Slf4j
@Service
public class QuickChatApplyServiceImpl extends ServiceImpl<QuickChatApplyMapper, QuickChatApply> implements QuickChatApplyService {
    @Autowired
    private QuickChatApplyStore applyStore;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatContactStore contactStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;
    @Value("${quick-chat.group.size}")
    private Integer groupSizeLimit;

    @Override
    public List<QuickChatApply> getApplyList() {
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        return applyStore.getListByToId(loginAccountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agreeApply(Long applyId) {
        QuickChatApply apply = applyStore.getByApplyId(applyId);
        if (ObjectUtils.isEmpty(apply)) {
            throw new QuickException(ResponseEnum.APPLY_NOT_EXIST);
        }
        if (YesNoEnum.YES.getCode().equals(apply.getStatus()) ||
                YesNoEnum.NO.getCode().equals(apply.getStatus())) {
            throw new QuickException(ResponseEnum.APPLY_IS_FINISH);
        }

        if (SessionTypeEnum.GROUP.getCode().equals(apply.getType())) {
            List<QuickChatGroupMember> members = memberStore.getListByGroupId(apply.getGroupId());
            if (groupSizeLimit < members.size() + 1) {
                throw new QuickException(ResponseEnum.GROUP_SIZE_OVER);
            }
            QuickChatGroupMember member = GroupMemberAdapter.buildMemberPO(apply.getGroupId(), apply.getToId());
            memberStore.saveMember(member);
            QuickChatContact contact = ContactAdapter.buildContactPO
                    (apply.getToId(), apply.getGroupId(), SessionTypeEnum.GROUP.getCode());
            contactStore.saveContact(contact);
            QuickChatSession session = SessionAdapter.buildSessionPO
                    (apply.getToId(), apply.getGroupId().toString(), apply.getGroupId(), apply.getType());
            sessionStore.saveInfo(session);
            Message<QuickChatApply> message = MessageBuilder.withPayload(apply).build();
            rocketMQTemplate.convertAndSend(RocketMQConstant.GROUP_ADD_MEMBER_NOTICE, message);

        } else if (SessionTypeEnum.SINGLE.getCode().equals(apply.getType())) {
            String fromId = apply.getFromId();
            String toId = apply.getToId();
            QuickChatContact contact1 = ContactAdapter.buildContactPO(fromId, Long.valueOf(toId), apply.getType());
            QuickChatContact contact2 = ContactAdapter.buildContactPO(toId, Long.valueOf(fromId), apply.getType());
            contactStore.saveContactList(Arrays.asList(contact1, contact2));
            Long relationId = IdWorker.getId();
            QuickChatSession session1 = SessionAdapter.buildSessionPO(fromId, toId, relationId, apply.getType());
            QuickChatSession session2 = SessionAdapter.buildSessionPO(toId, fromId, relationId, apply.getType());
            sessionStore.saveSessionList(Arrays.asList(session1, session2));
            rocketMQTemplate.asyncSend(RocketMQConstant.FRIEND_APPLY_TOPIC, MessageBuilder.withPayload(apply).build(),
                    new SendCallback() {
                        @Override
                        public void onSuccess(SendResult sendResult) {
                            log.info("-------------rocketmq message send successful: {}------------", sendResult);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            log.error("-------------rocketmq message send failed: {}------------", throwable.toString());
                        }
                    }
            );
        }
        applyStore.updateApplyStatus(applyId, YesNoEnum.YES.getCode());
    }

    @Override
    public void deleteApply(Long applyId) {
        applyStore.deleteByApplyId(applyId);
    }
}
