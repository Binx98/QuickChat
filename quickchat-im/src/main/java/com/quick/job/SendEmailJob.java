package com.quick.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2023/12/25 21:02
 * @Version 1.0
 * @Description: SendEmailJob
 */
@Component
public class SendEmailJob {
    /**
     * TODO 超过3天未登录，给他发邮件：你已经三天没登陆QuickChat了，老奴很想你（JIU）
     */
    @XxlJob("SendToOver3DayNotLogin")
    public ReturnT sendToOver3DayNotLogin() {
        return ReturnT.SUCCESS;
    }
}
