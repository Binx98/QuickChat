package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.response.R;
import com.quick.strategy.chatmsg.AbstractChatMsgStrategy;
import com.quick.strategy.chatmsg.ChatMsgStrategyFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:35
 * @Version 1.0
 * @Description: 聊天相关
 */
@RestController("/chat/msg")
public class ChatMsgController {
    /**
     * 发送消息
     */
    @PostMapping("/send")
    public R sendMessage(@RequestBody ChatMsgDTO msgDTO) {
        AbstractChatMsgStrategy chatMsgHandler = ChatMsgStrategyFactory.getStrategyHandler(msgDTO.getType());
        chatMsgHandler.sendChatMsg(msgDTO);
        return R.out(ResponseEnum.SUCCESS);
    }
}
