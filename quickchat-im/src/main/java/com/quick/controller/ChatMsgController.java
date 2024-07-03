package com.quick.controller;

import com.quick.annotation.RateLimiter;
import com.quick.enums.LimitTypeEnum;
import com.quick.enums.ResponseEnum;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.vo.ChatMsgVO;
import com.quick.response.R;
import com.quick.service.QuickChatMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:35
 * @Version 1.0
 * @Description: 聊天消息相关
 */
@Api(tags = "聊天信息")
@RestController
@RequestMapping("/msg")
public class ChatMsgController {
    @Autowired
    private QuickChatMsgService msgService;

    @ApiOperation("发送消息")
    @PostMapping("/send")
    @RateLimiter(time = 3, count = 5, limitType = LimitTypeEnum.IP)
    public R sendMsg(@RequestBody ChatMsgDTO msgDTO) throws Throwable {
        msgService.sendMsg(msgDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("根据 account_id 列表查询聊天记录")
    @PostMapping("/list")
    public R list(@RequestBody List<String> accountIds) {
        Map<String, List<ChatMsgVO>> result = msgService.getByAccountIds(accountIds);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("查询双方聊天记录")
    @GetMapping("/getByRelationId")
    public R chatMsgList(String relationId, Integer current, Integer size) {
        Map<String, List<ChatMsgVO>> resultMap = msgService.getByRelationId(relationId, current, size);
        return R.out(ResponseEnum.SUCCESS, resultMap);
    }

    @ApiOperation("对方正在输入")
    @GetMapping("/writing")
    public R writing(String fromId, String toId) {
        msgService.writing(fromId, toId);
        return R.out(ResponseEnum.SUCCESS);
    }
}
