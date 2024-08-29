package com.quick.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.adapter.ApplyAdapter;
import com.quick.adapter.UserAdapter;
import com.quick.constant.KafkaConstant;
import com.quick.enums.ApplyTypeEnum;
import com.quick.enums.ResponseEnum;
import com.quick.enums.YesNoEnum;
import com.quick.exception.QuickException;
import com.quick.kafka.KafkaProducer;
import com.quick.mapper.QuickChatGroupMemberMapper;
import com.quick.pojo.po.QuickChatApply;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickChatGroupMemberService;
import com.quick.store.QuickChatApplyStore;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatApplyStore applyStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;
    @Value("${quick-chat.group.invite-count}")
    private Integer inviteCountLimit;


    @Override
    public List<ChatUserVO> getGroupMemberList(Long groupId) {
        // 根据 group_id 查询所有群成员 account_id 列表
        List<QuickChatGroupMember> members = memberStore.getListByGroupId(groupId);
        List<String> accountIdList = members.stream()
                .map(QuickChatGroupMember::getAccountId)
                .collect(Collectors.toList());

        // 根据群成员 account_id 列表查询群成员用户列表信息
        List<QuickChatUser> userList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accountIdList)) {
            userList = userStore.getListByAccountIds(accountIdList);
        }
        return UserAdapter.buildUserVOList(userList);
    }

    @Override
    public void addMember(Long groupId, List<String> accountIdList) {
        // 添加群成员人数限制
        if (CollectionUtils.isEmpty(accountIdList)) {
            throw new QuickException(ResponseEnum.GROUP_MEMEBER_COUNT_NOT_EXIST);
        }
        if (CollectionUtils.isNotEmpty(accountIdList) && accountIdList.size() > inviteCountLimit) {
            throw new QuickException(ResponseEnum.GROUP_MEMBER_ADD_COUNT_NOT_ALLOW);
        }

        // 查询群组信息
        QuickChatGroup chatGroup = groupStore.getByGroupId(groupId);
        if (ObjectUtils.isEmpty(chatGroup)) {
            throw new QuickException(ResponseEnum.GROUP_NOT_EXIST);
        }

        // 不允许群成员邀请 && 操作者是群成员
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        if (YesNoEnum.NO.getCode().equals(chatGroup.getInvitePermission())
                && !loginAccountId.equals(chatGroup.getAccountId())) {
            throw new QuickException(ResponseEnum.GROUP_MEMBER_NOT_ALLOW);
        }

        // 去除已经在群的id
        List<QuickChatGroupMember> groupMemberByAccountId = memberStore.getGroupMemberByAccountId(groupId, accountIdList);
        List<String> savedAccountIdList = groupMemberByAccountId.stream()
                .map(QuickChatGroupMember::getAccountId)
                .collect(Collectors.toList());
        accountIdList.removeAll(savedAccountIdList);

        // 保存申请记录
        List<QuickChatApply> applyList = new ArrayList<>();
        for (String accountId : accountIdList) {
            QuickChatApply apply = ApplyAdapter.buildFriendApplyPO(loginAccountId, accountId, "邀请您加入群聊: "
                    + chatGroup.getGroupName(), ApplyTypeEnum.GROUP.getCode(), groupId, YesNoEnum.NO.getCode());
            applyList.add(apply);
        }

        // 批量保存申请记录列表
        applyStore.saveAll(applyList);

        // 推送给被邀请人
        kafkaProducer.send(KafkaConstant.GROUP_APPLY_TOPIC, JSONUtil.toJsonStr(applyList));
    }

    @Override
    public void deleteMember(Long groupId, String accountId) {
        // 判断当前操作是否是群主
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatGroup groupPO = groupStore.getByGroupId(groupId);
        if (ObjectUtils.isEmpty(groupPO) || groupPO.getAccountId().equals(loginAccountId)) {
            throw new QuickException(ResponseEnum.NOT_GROUP_OWNER);
        }

        // 删除群成员
        QuickChatGroupMember member = memberStore.getMemberByAccountId(groupId, accountId);
        memberStore.deleteByGroupIdAndAccountId(groupId, accountId);

        // 推送给被邀请人
        kafkaProducer.send(KafkaConstant.GROUP_DELETE_MEMBER_NOTICE, JSONUtil.toJsonStr(member));
    }
}
