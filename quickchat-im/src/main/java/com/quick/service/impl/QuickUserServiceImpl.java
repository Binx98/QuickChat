package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.quick.adapter.ChatSessionAdapter;
import com.quick.adapter.GroupMemberAdapter;
import com.quick.adapter.UserAdapter;
import com.quick.constant.RedisConstant;
import com.quick.enums.ResponseEnum;
import com.quick.enums.YesNoEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickUserMapper;
import com.quick.pojo.dto.EmailDTO;
import com.quick.pojo.dto.LoginDTO;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.dto.UserUpdateDTO;
import com.quick.pojo.po.QuickChatGroup;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickUserService;
import com.quick.store.QuickChatGroupMemberStore;
import com.quick.store.QuickChatGroupStore;
import com.quick.store.QuickChatSessionStore;
import com.quick.store.QuickChatUserStore;
import com.quick.strategy.email.AbstractEmailStrategy;
import com.quick.strategy.email.EmailStrategyFactory;
import com.quick.utils.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2023-11-13
 */
@Service
public class QuickUserServiceImpl extends ServiceImpl<QuickUserMapper, QuickChatUser> implements QuickUserService {
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private QuickChatGroupStore groupStore;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${quick_chat.common_group_id}")
    private String commonGroupId;

    /**
     * 根据 account_id 查询用户信息
     */
    @Override
    public ChatUserVO getByAccountId(String accountId) {
        QuickChatUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.USER_NOT_EXIST);
        }
        return UserAdapter.buildUserVO(userPO);
    }

    /**
     * 注册账号
     */
    @Override
    @Transactional
    public Boolean register(RegisterDTO registerDTO) throws Exception {
        // 两次密码输入是否一致
        if (!registerDTO.getPassword1().equals(registerDTO.getPassword2())) {
            throw new QuickException(ResponseEnum.PASSWORD_DIFF);
        }

        // 判断邮箱验证码
        String cacheEmailCode = redisUtil.getCacheObject(RedisConstant.EMAIL_KEY + registerDTO.getToEmail());
        if (StringUtils.isEmpty(cacheEmailCode) || !registerDTO.getEmailCode().equalsIgnoreCase(cacheEmailCode)) {
            throw new QuickException(ResponseEnum.EMAIL_CODE_ERROR);
        }

        // 判断账号是否存在
        QuickChatUser userPO = userStore.getByAccountId(registerDTO.getAccountId());
        if (ObjectUtils.isNotEmpty(userPO)) {
            throw new QuickException(ResponseEnum.ACCOUNT_ID_EXIST);
        }

        // 加入全员群聊、保存会话
        QuickChatGroupMember memberPO = GroupMemberAdapter.buildMemberPO(commonGroupId, registerDTO.getAccountId());
        memberStore.enterGroup(memberPO);
        QuickChatSession chatSession = ChatSessionAdapter.buildSessionPO(registerDTO.getAccountId(), commonGroupId.toString());
        sessionStore.saveInfo(chatSession);

        // 修改群聊信息（群成员数量）
        QuickChatGroup chatGroup = groupStore.getByGroupId(commonGroupId);
        chatGroup.setMemberCount(chatGroup.getMemberCount() + 1);
        groupStore.updateInfo(chatGroup);

        // 解析地址、保存账号信息
        String location = IpUtil.getIpAddr(HttpContextUtil.getRequest());
        userPO = UserAdapter.buildUserPO(registerDTO.getAccountId(), registerDTO.getPassword1(),
                registerDTO.getToEmail(), location, YesNoEnum.NO.getStatus());
        return userStore.saveUser(userPO);
    }

    /**
     * 登陆账号
     */
    @Override
    public String login(LoginDTO loginDTO) throws Exception {
        // 判断账号是否存在
        QuickChatUser userPO = userStore.getByAccountId(loginDTO.getAccountId());
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.USER_NOT_EXIST);
        }

        // 校验图片验证码
        String captchaKey = HttpContextUtil.getRequest().getHeader(RedisConstant.CAPTCHA_KEY);
        String cacheImgCode = redisUtil.getCacheObject(captchaKey);
        if (StringUtils.isEmpty(cacheImgCode) || !cacheImgCode.equalsIgnoreCase(loginDTO.getVerifyCode())) {
            throw new QuickException(ResponseEnum.IMG_CODE_ERROR);
        }

        // 判断密码是否正确
        String encryptPwd = AESUtil.encrypt(loginDTO.getPassWord());
        if (!encryptPwd.equals(userPO.getPassword())) {
            throw new QuickException(ResponseEnum.PASSWORD_DIFF);
        }

        // 登录成功，切换用户状态：已上线
        userPO.setLineStatus(YesNoEnum.YES.getStatus());
        userStore.updateInfo(userPO);

        // 解析当前登录地址，同步到用户信息
        String location = IpUtil.getIpAddr(HttpContextUtil.getRequest());
        if (!location.equals(userPO.getLocation())) {
            userPO.setLocation(location);
            userStore.updateInfo(userPO);
        }

        // 封装结果，返回
        return JwtUtil.generate(loginDTO.getAccountId());
    }

    /**
     * 生成验证码
     */
    @Override
    public void captcha() throws IOException {
        // 封装响应信息
        HttpServletResponse response = HttpContextUtil.getResponse();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        // 生成Cookie通过Response传给前端
        String captcha = RedisConstant.CAPTCHA_KEY + ":" + UUID.randomUUID();
        CookieUtil.addCookie(response, RedisConstant.CAPTCHA_KEY, captcha, false, -1, "/");

        // 验证码缓存到Redis（3min）
        String verifyCode = defaultKaptcha.createText();
        redisUtil.setCacheObject(captcha, verifyCode, 2, TimeUnit.MINUTES);

        // 将图片输出到页面
        ServletOutputStream outputStream = null;
        BufferedImage image = defaultKaptcha.createImage(verifyCode);
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 发送验证码邮件
     */
    @Override
    public Boolean sendEmail(EmailDTO emailDTO) throws MessagingException, IOException {
        AbstractEmailStrategy emailStrategy = EmailStrategyFactory.getStrategyHandler(emailDTO.getType());
        return emailStrategy.sendEmail(emailDTO);
    }

    /**
     * 修改用户信息
     */
    @Override
    public Boolean updateUser(UserUpdateDTO userDTO) {
        QuickChatUser userPO = UserAdapter.buildUserPO(userDTO);
        return userStore.updateInfo(userPO);
    }

    /**
     * 根据 token 查询用户信息
     */
    @Override
    public QuickChatUser getByToken() {
        // 请求头获取 token
        String token = HttpContextUtil.getRequest().getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new QuickException(ResponseEnum.USER_NOT_EXIST);
        }

        // 解析 token、查询用户信息
        Map<String, Object> resultMap = JwtUtil.resolve(token);
        String accountId = (String) resultMap.get(RequestContextUtil.ACCOUNT_ID);
        QuickChatUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.USER_NOT_EXIST);
        }
        return userPO;
    }
}
