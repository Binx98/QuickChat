package com.quick.service.impl;

import cn.hutool.json.JSONUtil;
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
import com.quick.mapper.QuickChatFriendContactMapper;
import com.quick.pojo.po.QuickChatApply;
import com.quick.pojo.po.QuickChatContact;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickChatFriendContactService;
import com.quick.store.QuickChatApplyStore;
import com.quick.store.QuickChatFriendContactStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.store.QuickChatUserStore;
import com.quick.utils.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 通讯录-好友 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Service
public class QuickChatFriendContactServiceImpl extends ServiceImpl<QuickChatFriendContactMapper, QuickChatContact> implements QuickChatFriendContactService {
    @Autowired
    private QuickChatFriendContactStore friendContactStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatApplyStore applyStore;
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public List<ChatUserVO> getFriendList() {
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatContact> friendList = friendContactStore.getListByFromId(loginAccountId);
        List<String> accountIds = friendList.stream()
                .map(item -> item.getToId())
                .collect(Collectors.toList());
        List<QuickChatUser> userList = userStore.getListByAccountIds(accountIds);
        return UserAdapter.buildUserVOList(userList);
    }

    @Override
    public Boolean addFriend(String toId, String applyInfo) {
        // 查询当前用户是否是好友
        String loginAccountId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatContact friendPO = friendContactStore.getByFromIdAndToId(loginAccountId, toId);
        if (ObjectUtils.isNotEmpty(friendPO)) {
            throw new QuickException(ResponseEnum.IS_YOUR_FRIEND);
        }

        // 保存好友申请记录
        QuickChatApply apply = ApplyAdapter.buildFriendApplyPO(loginAccountId,
                toId, applyInfo, ApplyTypeEnum.FRIEND.getCode(), YesNoEnum.NO.getCode());
        applyStore.saveInfo(apply);

        // 推送给目标用户
        kafkaProducer.send(KafkaConstant.FRIEND_APPLY_TOPIC, JSONUtil.toJsonStr(apply));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFriend(String toId) {
        // 查询当前用户是否是好友
        String fromId = (String) RequestContextUtil.getData().get(RequestContextUtil.ACCOUNT_ID);
        QuickChatContact friendPO = friendContactStore.getByFromIdAndToId(fromId, toId);
        if (ObjectUtils.isEmpty(friendPO)) {
            return true;
        }

        // 删除会话 + 通讯录好友
        friendContactStore.deleteByFromIdAndToId(fromId, toId);
        friendContactStore.deleteByFromIdAndToId(toId, fromId);
        return sessionStore.deleteByFromIdAndToId(fromId, toId);
    }
}
