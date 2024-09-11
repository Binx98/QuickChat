package com.quick.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.MsgAdapter;
import com.quick.constant.KafkaConstant;
import com.quick.enums.ResponseEnum;
import com.quick.enums.SessionTypeEnum;
import com.quick.exception.QuickException;
import com.quick.kafka.KafkaProducer;
import com.quick.mapper.QuickChatMsgMapper;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatContact;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.vo.ChatMsgVO;
import com.quick.service.QuickChatMsgService;
import com.quick.store.QuickChatContactStore;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatMsgStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import com.quick.strategy.msg.ChatMsgStrategyFactory;
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
@Service
public class QuickChatMsgServiceImpl extends ServiceImpl<QuickChatMsgMapper, QuickChatMsg> implements QuickChatMsgService {
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;
    @Autowired
    private QuickChatContactStore friendContactStore;

    @Override
    public Map<Long, List<ChatMsgVO>> getMsgByRelationId(Long relationId, Integer current, Integer size) {
        Page<QuickChatMsg> msgPage = msgStore.getByRelationId(relationId, current, size);
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
        // 根据 relationIds 查询聊天信息
        List<QuickChatMsg> msgList = msgStore.getByRelationIds(relationIds, size);
        if (CollectionUtils.isEmpty(msgList)) {
            return new HashMap<>(0);
        }
        List<ChatMsgVO> chatMsgVOList = MsgAdapter.buildChatMsgVOList(msgList);
        Map<Long, List<ChatMsgVO>> resultMap = chatMsgVOList.stream()
                .sorted(Comparator.comparing(ChatMsgVO::getCreateTime))
                .collect(Collectors.groupingBy(ChatMsgVO::getRelationId));

        // 没有消息的 relation_id 需要空集合占位（首次发送消息需要占位）
        for (Long relationId : relationIds) {
            if (!resultMap.containsKey(relationId)) {
                resultMap.put(relationId, new ArrayList<>());
            }
        }
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 查询对方是否是你的好友
        Integer sessionType = msgDTO.getSessionType();
        if (SessionTypeEnum.SINGLE.getCode().equals(sessionType)) {
            QuickChatContact friend = friendContactStore.getByFromIdAndToId(msgDTO.getFromId(), msgDTO.getToId());
            if (ObjectUtils.isEmpty(friend)) {
                throw new QuickException(ResponseEnum.NOT_YOUR_FRIEND);
            }
        }

        // 处理通讯双方会话信息
        this.handleSession(sessionType, msgDTO.getRelationId());

        // 根据不同消息类型，发送消息
        AbstractChatMsgStrategy chatMsgHandler = ChatMsgStrategyFactory.getStrategyHandler(msgDTO.getMsgType());
        QuickChatMsg chatMsg = chatMsgHandler.sendMsg(msgDTO);

        // 通过 Channel 推送给客户端（单聊、群聊）
        if (SessionTypeEnum.SINGLE.getCode().equals(sessionType)) {
            kafkaProducer.send(KafkaConstant.SEND_CHAT_SINGLE_MSG, JSONUtil.toJsonStr(chatMsg));
        } else if (SessionTypeEnum.GROUP.getCode().equals(sessionType)) {
            kafkaProducer.send(KafkaConstant.SEND_CHAT_GROUP_MSG, JSONUtil.toJsonStr(chatMsg));
        }
    }

    @Override
    public void writing(String fromId, String toId) {
        Map<String, String> param = new HashMap<>();
        param.put("fromId", fromId);
        param.put("toId", toId);
        kafkaProducer.send(KafkaConstant.SEND_CHAT_ENTERING, JSONUtil.toJsonStr(param));
    }

    private void handleSession(Integer sessionType, Long relationId) {
        // 根据 relation_id 查询所有会话列表（包括已删除的）
        List<QuickChatSession> sessionList = sessionStore.getAllBySessionId(relationId);
        if (CollectionUtils.isEmpty(sessionList)) {
            throw new QuickException(ResponseEnum.SESSION_INFO_ERROR);
        }

        // 针对单聊会话：确保会话数量 = 2
        if (SessionTypeEnum.SINGLE.getCode().equals(sessionType)) {
            if (sessionList.size() != 2) {
                throw new QuickException(ResponseEnum.SESSION_INFO_ERROR);
            }
        }

        // 针对群聊会话：确保会话数量 = 群成员总数
        else if (SessionTypeEnum.GROUP.getCode().equals(sessionType)) {
            List<QuickChatGroupMember> memberList = memberStore.getListByGroupId(relationId);
            if (sessionList.size() != memberList.size()) {
                throw new QuickException(ResponseEnum.SESSION_INFO_ERROR);
            }
        }

        // 批量恢复会话状态
        sessionList = sessionList.stream()
                .filter(item -> item.getDeleted().equals(true))
                .collect(Collectors.toList());
        List<QuickChatSession> needHandleList = new ArrayList<>();
        for (QuickChatSession session : sessionList) {
            if (session.getDeleted()) {
                session.setDeleted(false);
                session.setUpdateTime(LocalDateTime.now());
                needHandleList.add(session);
            }
        }

        if (CollectionUtils.isNotEmpty(needHandleList)) {
            sessionStore.updateList(needHandleList);
        }
    }
}
