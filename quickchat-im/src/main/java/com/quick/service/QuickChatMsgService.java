package com.quick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.pojo.vo.ChatMsgVO;

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
    /**
     * 根据 relation_id 分页查询聊天信息
     *
     * @param relationId 关联id
     * @param current    当前页
     * @param size       每页条数
     * @return
     */
    Map<String, List<ChatMsgVO>> getByRelationId(String relationId, Integer current, Integer size);

    /**
     * 根据 account_id 列表查询聊天记录
     *
     * @param accountIds accountId列表
     * @return 聊天信息
     */
    Map<String, List<ChatMsgVO>> getByAccountIds(List<String> accountIds);

    /**
     * 发送聊天信息
     *
     * @param msgDTO 消息实体
     */
    void sendMsg(ChatMsgDTO msgDTO) throws Throwable;

    /**
     * 对方正在输入...
     *
     * @param fromId 发送方
     * @param toId   接收方
     */
    void entering(String fromId, String toId);
}
