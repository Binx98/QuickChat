package com.quick.strategy.email.handler;

import com.quick.constant.RedisConstant;
import com.quick.enums.EmailEnum;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.pojo.dto.EmailDTO;
import com.quick.pojo.po.QuickChatUser;
import com.quick.store.QuickChatUserStore;
import com.quick.strategy.email.AbstractEmailStrategy;
import com.quick.threadpool.MyThreadPoolExecutor;
import com.quick.utils.EmailUtil;
import com.quick.utils.RandomUtil;
import com.quick.utils.RedisUtil;
import org.apache.commons.lang3.ObjectUtils;
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
    @Autowired
    private QuickChatUserStore userStore;

    @Override
    protected EmailEnum getEnum() {
        return EmailEnum.VERIFY_CODE;
    }

    @Override
    @Async(MyThreadPoolExecutor.EMAIL_POOL_NAME)
    public Boolean sendEmail(EmailDTO emailDTO) throws MessagingException, IOException {
        QuickChatUser userPO = userStore.getByEmail(emailDTO.getToEmail());
        if (ObjectUtils.isNotEmpty(userPO)) {
            throw new QuickException(ResponseEnum.EMAIL_HAS_REGISTERED);
        }
        // 生成验证码，有效期 3min
        String code = RandomUtil.generate(4, 1);
        String emailKey = RedisConstant.EMAIL_KEY + emailDTO.getToEmail();
        redisUtil.setCacheObject(emailKey, code, 3, TimeUnit.MINUTES);

        // 读取HTML文本，替换%s
        String htmlContent = emailUtil.readHtmlText("/email/sendCode.html");
        htmlContent = String.format(htmlContent, code);

        // 发送HTML邮件
        emailUtil.sendHtmlMail(emailDTO.getToEmail(), "注册QuickChat账号", htmlContent);
        return true;
    }
}
