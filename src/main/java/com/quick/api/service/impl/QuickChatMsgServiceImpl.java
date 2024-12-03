package com.quick.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.api.mapper.QuickChatMsgMapper;
import com.quick.common.adapter.MsgAdapter;
import com.quick.common.constant.RocketMQConstant;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.enums.SessionTypeEnum;
import com.quick.common.enums.YesNoEnum;
import com.quick.common.exception.QuickException;
import com.quick.common.mq.producer.MyRocketMQTemplate;
import com.quick.common.pojo.dto.ChatMsgDTO;
import com.quick.common.pojo.po.QuickChatContact;
import com.quick.common.pojo.po.QuickChatGroupMember;
import com.quick.common.pojo.po.QuickChatMsg;
import com.quick.common.pojo.po.QuickChatSession;
import com.quick.common.pojo.vo.ChatMsgVO;
import com.quick.api.service.QuickChatMsgService;
import com.quick.api.store.QuickChatContactStore;
import com.quick.api.store.QuickChatGroupMemberStore;
import com.quick.api.store.QuickChatMsgStore;
import com.quick.api.store.QuickChatSessionStore;
import com.quick.common.strategy.msg.AbstractChatMsgStrategy;
import com.quick.common.strategy.msg.ChatMsgStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 聊天信息 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
@Slf4j
@Service
public class QuickChatMsgServiceImpl extends ServiceImpl<QuickChatMsgMapper, QuickChatMsg> implements QuickChatMsgService {
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private MyRocketMQTemplate rocketMQTemplate;
    @Autowired
    private QuickChatGroupMemberStore memberStore;
    @Autowired
    private QuickChatContactStore friendContactStore;

    @Override
    public Map<Long, List<ChatMsgVO>> getPageByRelationId(Long relationId, Integer current, Integer size) {
        Page<QuickChatMsg> msgPage = msgStore.getPageByRelationId(relationId, current, size);
        if (CollectionUtils.isEmpty(msgPage.getRecords())) {
            return new HashMap<>(0);
        }
        List<ChatMsgVO> chatMsgVOList = MsgAdapter.buildChatMsgVOList(msgPage.getRecords());
        Map<Long, List<ChatMsgVO>> resultMap = chatMsgVOList.stream()
                .sorted(Comparator.comparing(ChatMsgVO::getCreateTime))
                .collect(Collectors.groupingBy(ChatMsgVO::getRelationId));
        return resultMap;
    }

    @Override
    public Map<Long, List<ChatMsgVO>> getMsgByRelationIds(List<Long> relationIds, Integer size) {
        List<QuickChatMsg> msgList = msgStore.getByRelationIds(relationIds, size);
        if (CollectionUtils.isEmpty(msgList)) {
            return new HashMap<>(0);
        }
        List<ChatMsgVO> chatMsgVOList = MsgAdapter.buildChatMsgVOList(msgList);
        Map<Long, List<ChatMsgVO>> resultMap = chatMsgVOList.stream()
                .sorted(Comparator.comparing(ChatMsgVO::getCreateTime))
                .collect(Collectors.groupingBy(ChatMsgVO::getRelationId));
        for (Long relationId : relationIds) {
            if (!resultMap.containsKey(relationId)) {
                resultMap.put(relationId, new ArrayList<>());
            }
        }
        return resultMap;
    }

    @Override
    public Map<Long, List<ChatMsgVO>> getHisPageByRelationId(Long relationId, Integer current, Integer size) {
//        List<QuickChatMsg> msgList = msgDorisStore.getHisPageByRelationId(relationId, current, size).getRecords();
//        if (CollectionUtils.isEmpty(msgList)) {
//            return new HashMap<>(0);
//        }
//        List<ChatMsgVO> chatMsgVOList = MsgAdapter.buildChatMsgVOList(msgList);
//        Map<Long, List<ChatMsgVO>> resultMap = chatMsgVOList.stream()
//                .sorted(Comparator.comparing(ChatMsgVO::getCreateTime))
//                .collect(Collectors.groupingBy(ChatMsgVO::getRelationId));
//        return resultMap;
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        Integer sessionType = msgDTO.getSessionType();
        if (SessionTypeEnum.SINGLE.getCode().equals(sessionType)) {
            QuickChatContact friend = friendContactStore.getByFromIdAndToId(msgDTO.getFromId(), msgDTO.getToId());
            if (ObjectUtils.isEmpty(friend)) {
                throw new QuickException(ResponseEnum.NOT_YOUR_FRIEND);
            }
        }
        this.handleSession(sessionType, msgDTO.getRelationId());
        AbstractChatMsgStrategy chatMsgHandler = ChatMsgStrategyFactory.getStrategyHandler(msgDTO.getMsgType());
        QuickChatMsg chatMsg = chatMsgHandler.sendMsg(msgDTO);
        if (SessionTypeEnum.SINGLE.getCode().equals(sessionType)) {
            rocketMQTemplate.asyncSend(RocketMQConstant.SEND_CHAT_SINGLE_MSG, chatMsg);
        } else if (SessionTypeEnum.GROUP.getCode().equals(sessionType)) {
            rocketMQTemplate.asyncSend(RocketMQConstant.SEND_CHAT_GROUP_MSG, chatMsg);
        }
    }

    @Override
    public void writing(String fromId, String toId) {
        Map<String, String> param = new HashMap<>();
        param.put("fromId", fromId);
        param.put("toId", toId);
        rocketMQTemplate.asyncSend(RocketMQConstant.SEND_CHAT_ENTERING, param);
    }

    private void handleSession(Integer sessionType, Long relationId) {
        List<QuickChatSession> sessionList = sessionStore.getAllBySessionId(relationId);
        if (CollectionUtils.isEmpty(sessionList)) {
            throw new QuickException(ResponseEnum.SESSION_INFO_ERROR);
        }
        if (SessionTypeEnum.SINGLE.getCode().equals(sessionType)) {
            if (sessionList.size() != 2) {
                throw new QuickException(ResponseEnum.SESSION_INFO_ERROR);
            }
        } else if (SessionTypeEnum.GROUP.getCode().equals(sessionType)) {
            List<QuickChatGroupMember> memberList = memberStore.getListByGroupId(relationId);
            if (sessionList.size() != memberList.size()) {
                throw new QuickException(ResponseEnum.SESSION_INFO_ERROR);
            }
        }
        List<QuickChatSession> needHandleList = new ArrayList<>();
        sessionList = sessionList.stream()
                .filter(item -> YesNoEnum.NO.getCode().equals(item.getStatus()))
                .collect(Collectors.toList());
        for (QuickChatSession session : sessionList) {
            if (YesNoEnum.NO.getCode().equals(session.getStatus())) {
                session.setStatus(YesNoEnum.YES.getCode());
                session.setUpdateTime(LocalDateTime.now());
                needHandleList.add(session);
            }
        }
        if (CollectionUtils.isNotEmpty(needHandleList)) {
            sessionStore.updateList(needHandleList);
        }
    }
}
