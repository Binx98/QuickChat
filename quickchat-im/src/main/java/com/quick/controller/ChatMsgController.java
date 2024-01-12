package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.response.R;
import com.quick.service.QuickChatMsgService;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import com.quick.strategy.chatmsg.ChatMsgStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:35
 * @Version 1.0
 * @Description: 聊天相关
 */
@RestController
@RequestMapping("/chat/msg")
public class ChatMsgController {
    @Autowired
    private QuickChatMsgService msgService;

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public R sendMsg(@RequestBody ChatMsgDTO msgDTO) throws Throwable {
        AbstractChatMsgStrategy chatMsgHandler = ChatMsgStrategyFactory.getStrategyHandler(msgDTO.getMsgType());
        chatMsgHandler.sendChatMsg(msgDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 查询会话列表聊天记录（访问聊天页面）
     */
    @PostMapping("/list")
    public R list(@RequestBody List<String> accountIds) {
        Map<String, List<QuickChatMsg>> resultMap = msgService.getByAccountIds(accountIds);
        return R.out(ResponseEnum.SUCCESS, resultMap);
    }

    /**
     * 查询双方聊天记录
     */
    @GetMapping("/getByRelationId/{accountId}/{current}/{size}")
    public R chatMsgList(@PathVariable String accountId,
                         @PathVariable Integer current,
                         @PathVariable Integer size) {
        List<QuickChatMsg> chatMsg = msgService.getByRelationId(accountId, current, size);
        return R.out(ResponseEnum.SUCCESS, chatMsg);
    }

    /**
     * 撤回消息
     */
    @PostMapping("/recall")
    public R recallMsg() {
        return R.out(ResponseEnum.SUCCESS);
    }
}
