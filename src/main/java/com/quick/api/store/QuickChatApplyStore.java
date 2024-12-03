package com.quick.api.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.common.pojo.po.QuickChatApply;

import java.util.List;

/**
 * <p>
 * 好友/群聊申请 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-03-12
 */
public interface QuickChatApplyStore extends IService<QuickChatApply> {
    /**
     * 保存申请信息
     *
     * @param apply 申请信息
     * @return 执行结果
     */
    Boolean saveApply(QuickChatApply apply);

    /**
     * 根据 to_id 查询申请列表
     *
     * @param toId 接收方
     * @return 申请列表
     */
    List<QuickChatApply> getListByToId(String toId);

    /**
     * 修改申请状态
     *
     * @param applyId 申请id
     * @param toId    接收账号
     * @param status  状态
     * @return 执行结果
     */
    Boolean updateApplyStatus(Long applyId, String toId, Integer status);

    /**
     * 根据 申请id 删除申请记录
     *
     * @param applyId 申请id
     * @param toId    接收方账号
     * @return 执行结果
     */
    Boolean deleteByApplyId(Long applyId, String toId);

    /**
     * 根据 申请id 查询申请记录
     *
     * @param applyId 申请id
     * @return 申请记录
     */
    QuickChatApply getByApplyId(Long applyId);

    /**
     * 批量保存申请记录
     *
     * @param applyList 申请列表
     * @return
     */
    Boolean saveAll(List<QuickChatApply> applyList);

    /**
     * 根据 apply_id 删除 Redis 缓存
     *
     * @param applyId 申请id
     */
    void deleteCacheByApplyId(Long applyId);
}
