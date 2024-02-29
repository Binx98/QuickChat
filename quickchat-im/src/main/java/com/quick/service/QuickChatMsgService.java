package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.vo.ChatMsgVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 聊天信息 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
public interface QuickChatMsgService extends IService<QuickChatMsg> {

    Map<String, List<ChatMsgVO>> getByRelationId(String relationId, Integer current, Integer size);

    Map<String, List<ChatMsgVO>> getByAccountIds(List<String> accountIds) throws ExecutionException, InterruptedException;
}
