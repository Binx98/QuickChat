package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.response.R;
import com.quick.service.QuickChatMsgService;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import com.quick.strategy.chatmsg.ChatMsgStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:35
 * @Version 1.0
 * @Description: 聊天相关
 */
@RestController("/chat/msg")
public class ChatMsgController {
    @Autowired
    private QuickChatMsgService msgService;

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public R sendMsg(@RequestBody ChatMsgDTO msgDTO) {
        AbstractChatMsgStrategy chatMsgHandler = ChatMsgStrategyFactory.getStrategyHandler(msgDTO.getType());
        chatMsgHandler.sendChatMsg(msgDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 查询通讯双方聊天信息列表
     */
    @GetMapping("/getChatMsg/{accountId}")
    public R chatMsgList(@PathVariable String accountId) {
        msgService.getChatMsg(accountId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 撤回消息
     */
    @PostMapping("/recall")
    public R recallMsg() {
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * 点赞、点踩消息
     */
    @PostMapping("/like")
    public R likeMsg() {
        return R.out(ResponseEnum.SUCCESS);
    }
}
