package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatGroup;

import java.util.List;

/**
 * <p>
 * 群聊 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-01-08
 */
public interface QuickChatGroupStore extends IService<QuickChatGroup> {

    QuickChatGroup getByGroupId(String groupId);

    Boolean updateInfo(QuickChatGroup chatGroup);

    List<QuickChatGroup> getListByGroupIds(List<String> groupIds);
}
