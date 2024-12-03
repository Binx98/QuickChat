package com.quick.common.strategy.email.handler;

import com.quick.common.constant.RedisConstant;
import com.quick.common.enums.EmailEnum;
import com.quick.common.pojo.dto.EmailDTO;
import com.quick.common.strategy.email.AbstractEmailStrategy;
import com.quick.common.threadpool.MyThreadPoolExecutor;
import com.quick.common.utils.EmailUtil;
import com.quick.common.utils.RandomUtil;
import com.quick.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author 徐志斌
 * @Date: 2023/12/31 13:48
 * @Version 1.0
 * @Description: 发送验证码
 */
@Component
public class SendCodeHandler extends AbstractEmailStrategy {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private EmailUtil emailUtil;

    @Override
    protected EmailEnum getEnum() {
        return EmailEnum.VERIFY_CODE;
    }

    @Override
    @Async(MyThreadPoolExecutor.EMAIL_POOL_NAME)
    public void sendEmail(EmailDTO emailDTO) throws MessagingException, IOException {
        // 生成验证码，有效期 3min
        String code = RandomUtil.generate(4, 1);
        String emailKey = RedisConstant.EMAIL_KEY + emailDTO.getToEmail();
        redisUtil.setCacheObject(emailKey, code, 3, TimeUnit.MINUTES);

        // 读取HTML文本，替换%s
        String htmlContent = emailUtil.readHtmlText("/src/main/resources/email/sendCode.html");
        htmlContent = String.format(htmlContent, code);

        // 发送HTML邮件
        emailUtil.sendHtmlMail(emailDTO.getToEmail(), "注册QuickChat账号", htmlContent);
    }
}
