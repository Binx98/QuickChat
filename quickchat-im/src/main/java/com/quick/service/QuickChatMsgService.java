package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatMsg;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 聊天信息 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
public interface QuickChatMsgService extends IService<QuickChatMsg> {

    List<QuickChatMsg> getByRelationId(String relationId, Integer current, Integer size);

    Map<String, List<QuickChatMsg>> getMapByAccountIds(List<String> accountIds, Integer current, Integer size);
}
