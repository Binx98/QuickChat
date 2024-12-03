package com.quick.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.common.pojo.po.QuickChatApply;

import java.util.List;

/**
 * <p>
 * 申请通知 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-03-12
 */
public interface QuickChatApplyService extends IService<QuickChatApply> {

    /**
     * 查询申请通知列表
     *
     * @return 通知列表
     */
    List<QuickChatApply> getApplyList();

    /**
     * 同意申请
     *
     * @param applyId 申请id
     * @return 执行结果
     */
    void agreeApply(Long applyId);

    /**
     * 删除申请
     *
     * @param applyId 申请id
     * @return 执行结果
     */
    void deleteApply(Long applyId);
}
