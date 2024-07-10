package com.quick.strategy.email.handler;

import com.quick.constant.RedisConstant;
import com.quick.enums.EmailEnum;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.pojo.dto.EmailDTO;
import com.quick.pojo.po.QuickChatUser;
import com.quick.store.QuickChatUserStore;
import com.quick.strategy.email.AbstractEmailStrategy;
import com.quick.utils.EmailUtil;
import com.quick.utils.RandomUtil;
import com.quick.utils.RedisUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author 刘东辉
 * @Date 2024/7/9 15:24
 * @Description: TODO
 */
@Component
public class FindBackHandler extends AbstractEmailStrategy {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private QuickChatUserStore userStore;
    @Override
    protected EmailEnum getEnum() {
        return EmailEnum.FIND_BACK;
    }

    @Override
    public Boolean sendEmail(EmailDTO emailDTO) throws MessagingException, IOException {

        QuickChatUser userPO = userStore.getByEmail(emailDTO.getToEmail());
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.EMAIL_NOT_REGISTERED);
        }

        // 生成验证码，有效期 3min
        String code = RandomUtil.generate(4, 1);
        String emailKey = RedisConstant.EMAIL_KEY + emailDTO.getToEmail();
        redisUtil.setCacheObject(emailKey, code, 3, TimeUnit.MINUTES);

        // 读取HTML文本，替换%s
        String htmlContent = emailUtil.readHtmlText("/email/findBack.html");
        htmlContent = String.format(htmlContent, code);

        // 发送HTML邮件
        emailUtil.sendHtmlMail(emailDTO.getToEmail(), "找回QuickChat密码", htmlContent);
        return true;
    }
}
