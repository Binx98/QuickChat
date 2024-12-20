package com.quick.api.controller;

import com.quick.common.annotation.RateLimiter;
import com.quick.common.enums.LimitTypeEnum;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.pojo.dto.ChatMsgDTO;
import com.quick.common.pojo.vo.ChatMsgVO;
import com.quick.common.response.R;
import com.quick.api.service.QuickChatMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
public class QuickChatMsgController {
    @Autowired
    private QuickChatMsgService msgService;

    @ApiOperation("发送消息")
    @PostMapping("/send")
    @RateLimiter(time = 3, count = 5, limitType = LimitTypeEnum.IP)
    public R sendMsg(@Validated @RequestBody ChatMsgDTO msgDTO) throws Throwable {
        msgService.sendMsg(msgDTO);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("查询聊天记录（首次访问）")
    @PostMapping("/getByRelationIds")
    public R getMsgByRelationIdList(@RequestBody List<Long> relationIds, Integer size) {
        Map<Long, List<ChatMsgVO>> result = msgService.getMsgByRelationIds(relationIds, size);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("查询聊天记录（热数据）")
    @GetMapping("/getPageByRelationId/{relationId}/{current}/{size}")
    public R getPageByRelationId(@PathVariable Long relationId,
                                 @PathVariable Integer current,
                                 @PathVariable Integer size) {
        Map<Long, List<ChatMsgVO>> resultMap = msgService.getPageByRelationId(relationId, current, size);
        return R.out(ResponseEnum.SUCCESS, resultMap);
    }

    @ApiOperation("查询聊天记录（历史冷数据）")
    @GetMapping("/getHisPageByRelationId/{relationId}/{current}/{size}")
    public R getHisPageByRelationId(@PathVariable Long relationId,
                                    @PathVariable Integer current,
                                    @PathVariable Integer size) {
        Map<Long, List<ChatMsgVO>> resultMap = msgService.getHisPageByRelationId(relationId, current, size);
        return R.out(ResponseEnum.SUCCESS, resultMap);
    }

    @ApiOperation("对方正在输入")
    @GetMapping("/writing")
    public R writing(String fromId, String toId) {
        msgService.writing(fromId, toId);
        return R.out(ResponseEnum.SUCCESS);
    }
}
