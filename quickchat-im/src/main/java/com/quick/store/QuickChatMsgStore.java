package com.quick.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatMsg;

import java.util.List;

/**
 * <p>
 * 聊天信息 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-25
 */
public interface QuickChatMsgStore extends IService<QuickChatMsg> {
    /**
     * 保存聊天消息
     *
     * @param chatMsg 消息实体
     * @return 执行结果
     */
    Boolean saveMsg(QuickChatMsg chatMsg);

    /**
     * 根据 relation_id 分页查询聊天信息
     *
     * @param relationId 关联id
     * @param current    起始页
     * @param size       每页条数
     * @return 聊天信息结果
     */
    Page<QuickChatMsg> getByRelationId(String relationId, Integer current, Integer size);

    /**
     * 根据 relation_id集合 查询聊天信息
     *
     * @param relationIds relation_id集合
     * @return 聊天信息列表
     */
    List<QuickChatMsg> getByRelationIdList(List<String> relationIds);

    /**
     * 根据 msg_id 查询单条聊天信息
     *
     * @param msgId 消息id
     * @return 消息实体
     */
    QuickChatMsg getByMsgId(Long msgId);

    /**
     * 根据 msg_id 修改聊天信息
     *
     * @param chatMsg 消息实体
     * @return 执行结果
     */
    Boolean updateByMsgId(QuickChatMsg chatMsg);
}
