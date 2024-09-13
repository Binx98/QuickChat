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
import com.quick.mapper.QuickChatContactMapper;
import com.quick.pojo.po.QuickChatApply;
import com.quick.pojo.po.QuickChatContact;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickChatContactService;
import com.quick.store.QuickChatApplyStore;
import com.quick.store.QuickChatContactStore;
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
 * 通讯录 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-30
 */
@Service
public class QuickChatContactServiceImpl extends ServiceImpl<QuickChatContactMapper, QuickChatContact> implements QuickChatContactService {
    @Autowired
    private QuickChatContactStore friendContactStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatApplyStore applyStore;
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public List<ChatUserVO> getContactList() {
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        List<QuickChatContact> friendList = friendContactStore.getListByFromId(loginAccountId);
        List<String> accountIds = friendList.stream()
                .map(item -> item.getToId())
                .collect(Collectors.toList());
        List<QuickChatUser> userList = userStore.getListByAccountIds(accountIds);
        return UserAdapter.buildUserVOList(userList);
    }

    @Override
    public void addFriend(String accountId, String applyInfo) {
        String loginAccountId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        QuickChatContact friendPO = friendContactStore.getByFromIdAndToId(loginAccountId, accountId);
        if (ObjectUtils.isNotEmpty(friendPO)) {
            throw new QuickException(ResponseEnum.YOUR_FRIEND);
        }
        QuickChatApply apply = ApplyAdapter.buildFriendApplyPO(loginAccountId, accountId,
                applyInfo, ApplyTypeEnum.FRIEND.getCode(), null, YesNoEnum.NO.getCode());
        applyStore.saveApply(apply);
        kafkaProducer.send(KafkaConstant.FRIEND_APPLY_TOPIC, JSONUtil.toJsonStr(apply));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(String toId) {
        String fromId = (String) RequestContextUtil.getData(RequestContextUtil.ACCOUNT_ID);
        QuickChatContact friendPO = friendContactStore.getByFromIdAndToId(fromId, toId);
        if (ObjectUtils.isEmpty(friendPO)) {
            return;
        }
        friendContactStore.deleteByFromIdAndToId(fromId, toId);
        friendContactStore.deleteByFromIdAndToId(toId, fromId);
        sessionStore.deleteByFromIdAndToId(fromId, toId);
    }
}
