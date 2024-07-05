package com.quick.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatApply;

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
    Boolean saveInfo(QuickChatApply apply);
}
