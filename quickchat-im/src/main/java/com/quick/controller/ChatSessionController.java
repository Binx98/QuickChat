package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.vo.ChatSessionVO;
import com.quick.response.R;
import com.quick.service.QuickChatSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2023/11/19 11:11
 * @Version 1.0
 * @Description: 聊天会话
 */
@Api(tags = "会话相关")
@RestController
@RequestMapping("/chat/session")
public class ChatSessionController {
    @Autowired
    private QuickChatSessionService sessionService;

    @ApiOperation("查询会话列表")
    @GetMapping("/list")
    public R getSessionList() {
        List<ChatSessionVO> result = sessionService.getSessionList();
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("查询单个会话")
    @GetMapping("/getSessionInfo/{fromId}/{toId}")
    public R getSessionInfo(@PathVariable String fromId, @PathVariable String toId) {
        ChatSessionVO result = sessionService.getSessionInfo(fromId, toId);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("修改会话已读时间")
    @PostMapping("/updateReadTime/{sessionId}")
    public R updateSession(@PathVariable Long sessionId) {
        sessionService.updateLastReadTime(sessionId);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("删除聊天会话")
    @DeleteMapping("/delete/{sessionId}")
    public R deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
        return R.out(ResponseEnum.SUCCESS);
    }

    /**
     * TODO 置顶会话
     */
}
