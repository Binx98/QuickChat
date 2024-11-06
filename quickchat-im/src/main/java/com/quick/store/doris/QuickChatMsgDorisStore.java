package com.quick.store.doris;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.pojo.po.QuickChatMsg;

import java.util.List;

/**
 * <p>
 * 聊天信息 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-11-06
 */
public interface QuickChatMsgDorisStore extends IService<QuickChatMsg> {
    /**
     * 批量保存聊天记录
     *
     * @param msgList 消息列表
     * @return 执行结果
     */
    Boolean saveBatchMsg(List<QuickChatMsg> msgList);
}
