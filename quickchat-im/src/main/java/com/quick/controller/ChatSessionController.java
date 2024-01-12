package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.vo.ChatSessionVO;
import com.quick.response.R;
import com.quick.service.QuickChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author 徐志斌
 * @Date: 2023/11/19 11:11
 * @Version 1.0
 * @Description: 聊天会话
 */
@RestController
@RequestMapping("/chat/session")
public class ChatSessionController {
    @Autowired
    private QuickChatSessionService sessionService;

    /**
     * 查询会话列表
     */
    @GetMapping("/list")
    public R getSessionList() throws ExecutionException, InterruptedException {
        List<ChatSessionVO> result = sessionService.getSessionList();
        return R.out(ResponseEnum.SUCCESS, result);
    }

    /**
     * 删除聊天会话
     */
    @DeleteMapping("/delete/{sessionId}")
    public R deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
        return R.out(ResponseEnum.SUCCESS);
    }
}
