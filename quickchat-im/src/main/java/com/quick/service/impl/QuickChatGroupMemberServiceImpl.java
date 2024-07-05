package com.quick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.UserAdapter;
import com.quick.constant.KafkaConstant;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.kafka.KafkaProducer;
import com.quick.mapper.QuickChatGroupMemberMapper;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickChatGroupMemberService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 群成员 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
@Service
public class QuickChatGroupMemberServiceImpl extends ServiceImpl<QuickChatGroupMemberMapper, QuickChatGroupMember> implements QuickChatGroupMemberService {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Override
    public List<ChatUserVO> getGroupMemberList(Long groupId) {
        List<QuickChatGroupMember> members = memberStore.getListByGroupId(groupId);
        List<String> accountIdList = members.stream()
                .map(QuickChatGroupMember::getAccountId)
                .collect(Collectors.toList());
        List<QuickChatUser> userList = userStore.getListByAccountIds(accountIdList);
        return UserAdapter.buildUserVOList(userList);
    }

    @Override
    public Boolean addMember(Long groupId, List<String> accountIdList) {
        return null;
    }

    @Override
    public Boolean removeMember(Long groupId, String accountId) {
        // 判断当前操作是否是群主
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroup groupPO = groupStore.getByGroupId(groupId.toString());
        if (ObjectUtils.isEmpty(groupPO) || groupPO.getAccountId().equals(loginAccountId)) {
            throw new QuickException(ResponseEnum.FAIL);
        }

        // 删除群成员 + 通知目标用户被移除群聊
        memberStore.deleteByGroupIdAndAccountId(groupId, accountId);
        kafkaProducer.send(KafkaConstant.SEND_CHAT_GROUP_MSG, null);
        return null;
    }
}
