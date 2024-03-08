package com.quick.controller;

import com.quick.annotation.RateLimiter;
import com.quick.enums.LimitType;
import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.vo.ChatMsgVO;
import com.quick.response.R;
import com.quick.service.QuickChatMsgService;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import com.quick.strategy.msg.ChatMsgStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:35
 * @Version 1.0
 * @Description: 聊天消息相关
 */
@RestController
@RequestMapping("/chat/msg")
public class ChatMsgController {
    @Autowired
    private QuickChatMsgService msgService;

    /**
     * 发送聊天消息
     */
    @PostMapping("/send")
    @RateLimiter(time = 3, count = 5, limitType = LimitType.IP)
    public R sendMsg(@RequestBody ChatMsgDTO msgDTO) throws Throwable {
        AbstractChatMsgStrategy chatMsgHandler = ChatMsgStrategyFactory.getStrategyHandler(msgDTO.getMsgType());
        chatMsgHandler.sendChatMsg(msgDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 查询会话列表聊天记录（访问聊天页面）
     */
    @PostMapping("/list")
    public R list(@RequestBody List<String> accountIds) throws ExecutionException, InterruptedException {
        Map<String, List<ChatMsgVO>> result = msgService.getByAccountIds(accountIds);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    /**
     * 查询双方聊天记录
     */
    @GetMapping("/getByRelationId/{relationId}/{current}/{size}")
    public R chatMsgList(@PathVariable String relationId,
                         @PathVariable Integer current,
                         @PathVariable Integer size) {
        Map<String, List<ChatMsgVO>> resultMap = msgService.getByRelationId(relationId, current, size);
        return R.out(ResponseEnum.SUCCESS, resultMap);
    }

    /**
     * 删除消息
     * TODO：❌暂时无法落地。原因：Web系统数据存储在MySQL中，群聊场景千人千面展示代价太大！
     */
    @DeleteMapping("/delete/{msgId}")
    public R deleteMsg(@PathVariable Long msgId) {
        return R.out(ResponseEnum.SUCCESS);
    }
}
