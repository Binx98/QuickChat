package com.quick.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ContactAdapter;
import com.quick.adapter.GroupMemberAdapter;
import com.quick.adapter.SessionAdapter;
import com.quick.constant.KafkaConstant;
import com.quick.enums.ResponseEnum;
import com.quick.enums.SessionTypeEnum;
import com.quick.enums.YesNoEnum;
import com.quick.exception.QuickException;
import com.quick.kafka.KafkaProducer;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Service
public class QuickChatApplyServiceImpl extends ServiceImpl<QuickChatApplyMapper, QuickChatApply> implements QuickChatApplyService {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private QuickChatApplyStore applyStore;
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
        // 判断申请记录是否被处理过
        QuickChatApply apply = applyStore.getByApplyId(applyId);
        if (ObjectUtils.isEmpty(apply)) {
            throw new QuickException(ResponseEnum.APPLY_NOT_EXIST);
        }
        if (YesNoEnum.YES.getCode().equals(apply.getStatus()) ||
                YesNoEnum.NO.getCode().equals(apply.getStatus())) {
            throw new QuickException(ResponseEnum.APPLY_IS_FINISH);
        }

        // 同意申请
        applyStore.updateApplyStatus(applyId, YesNoEnum.YES.getCode());

        // 1.入群申请
        if (SessionTypeEnum.GROUP.getCode().equals(apply.getType())) {
            // 查询群成员数量，判断是否超过限制
            List<QuickChatGroupMember> members = memberStore.getListByGroupId(apply.getGroupId());
            if (groupSizeLimit < members.size() + 1) {
                throw new QuickException(ResponseEnum.GROUP_SIZE_OVER);
            }

            // 保存群成员、添加会话
            QuickChatGroupMember member = GroupMemberAdapter.buildMemberPO(apply.getGroupId(), apply.getToId());
            memberStore.saveMember(member);
            QuickChatSession session = SessionAdapter.buildSessionPO
                    (apply.getToId(), apply.getGroupId().toString(), apply.getGroupId(), apply.getType());
            sessionStore.saveInfo(session);

            // 推送给目标用户
            kafkaProducer.send(KafkaConstant.GROUP_ADD_MEMBER_NOTICE, JSONUtil.toJsonStr(apply));
        }

        // 2.好友申请
        else if (SessionTypeEnum.SINGLE.getCode().equals(apply.getType())) {
            // 保存通讯录
            String fromId = apply.getFromId();
            String toId = apply.getToId();
            QuickChatContact contact1 = ContactAdapter.buildContactPO(fromId, Long.valueOf(toId), apply.getType());
            QuickChatContact contact2 = ContactAdapter.buildContactPO(toId, Long.valueOf(fromId), apply.getType());
            contactStore.saveContactList(Arrays.asList(contact1, contact2));

            // 保存聊天会话
            Long relationId = IdWorker.getId();
            QuickChatSession session1 = SessionAdapter.buildSessionPO(fromId, toId, relationId, apply.getType());
            QuickChatSession session2 = SessionAdapter.buildSessionPO(toId, fromId, relationId, apply.getType());
            sessionStore.saveSessionList(Arrays.asList(session1, session2));

            // 推送给目标用户
            kafkaProducer.send(KafkaConstant.FRIEND_APPLY_TOPIC, JSONUtil.toJsonStr(apply));
        }
    }

    @Override
    public void deleteApply(Long applyId) {
        applyStore.deleteByApplyId(applyId);
    }
}
