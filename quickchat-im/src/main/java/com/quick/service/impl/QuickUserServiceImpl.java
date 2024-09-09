package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.quick.adapter.GroupMemberAdapter;
import com.quick.adapter.SessionAdapter;
import com.quick.adapter.UserAdapter;
import com.quick.constant.RedisConstant;
import com.quick.enums.GenderEnum;
import com.quick.enums.ResponseEnum;
import com.quick.enums.SessionTypeEnum;
import com.quick.enums.YesNoEnum;
import com.quick.exception.QuickException;
import com.quick.kafka.KafkaProducer;
import com.quick.mapper.QuickChatUserMapper;
import com.quick.pojo.dto.*;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickUserService;
import com.quick.store.QuickChatGroupMemberStore;
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
import java.util.HashMap;
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
public class QuickUserServiceImpl extends ServiceImpl<QuickChatUserMapper, QuickChatUser> implements QuickUserService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private QuickChatUserStore userStore;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private QuickChatGroupMemberStore memberStore;

    @Value("${quick-chat.common-group-id}")
    private Long officialGroupId;
    @Value("${quick-chat.avatar.boy}")
    private String boyAvatar;
    @Value("${quick-chat.avatar.girl}")
    private String girlAvatar;

    @Override
    public ChatUserVO getByAccountId(String accountId) {
        QuickChatUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.ACCOUNT_ID_NOT_EXIST);
        }
        return UserAdapter.buildUserVO(userPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean register(RegisterFormDTO registerDTO) throws Exception {
        // 两次密码输入是否一致
        if (!registerDTO.getPassword1().equals(registerDTO.getPassword2())) {
            throw new QuickException(ResponseEnum.PASSWORD_DIFF);
        }

        // 判断邮箱验证码
        String cacheEmailCode = redisUtil.getCacheObject(RedisConstant.EMAIL_KEY + registerDTO.getEmail());
        if (StringUtils.isEmpty(cacheEmailCode) || !registerDTO.getEmailCode().equalsIgnoreCase(cacheEmailCode)) {
            throw new QuickException(ResponseEnum.EMAIL_CODE_ERROR);
        }

        // 判断账号是否存在
        QuickChatUser userPO = userStore.getByAccountId(registerDTO.getAccountId());
        if (ObjectUtils.isNotEmpty(userPO)) {
            throw new QuickException(ResponseEnum.ACCOUNT_ID_EXIST);
        }

        // 判断邮箱是否注册过
        if (registerDTO.getEmail().equals(userPO.getEmail())) {
            throw new QuickException(ResponseEnum.EMAIL_HAS_REGISTERED);
        }

        // 加入全员群聊、保存会话
        QuickChatGroupMember memberPO = GroupMemberAdapter.buildMemberPO(officialGroupId, registerDTO.getAccountId());
        memberStore.saveMember(memberPO);
        QuickChatSession chatSession = SessionAdapter.buildSessionPO(registerDTO.getAccountId(),
                officialGroupId.toString(), officialGroupId, SessionTypeEnum.GROUP.getCode());
        sessionStore.saveInfo(chatSession);

        // 解析地址信息、密码对称加密
        String location = IpUtil.getIpAddr(HttpServletUtil.getRequest());
        String password = AESUtil.encrypt(registerDTO.getPassword1());

        // 保存账号信息
        String avatar = GenderEnum.BOY.getType().equals(registerDTO.getGender()) ? boyAvatar : girlAvatar;
        userPO = UserAdapter.buildUserPO(registerDTO.getAccountId(), avatar, password,
                registerDTO.getGender(), registerDTO.getEmail(), location, YesNoEnum.NO.getCode());
        return userStore.saveUser(userPO);
    }

    @Override
    public Map<String, Object> login(LoginFormDTO loginDTO) throws Exception {
        // 判断账号是否存在
        QuickChatUser userPO = userStore.getByAccountId(loginDTO.getAccountId());
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.ACCOUNT_ID_NOT_EXIST);
        }

        // 校验图片验证码
        String captchaKey = HttpServletUtil.getRequest().getHeader(RedisConstant.CAPTCHA_KEY);
        String cacheImgCode = redisUtil.getCacheObject(captchaKey);
        if (StringUtils.isEmpty(cacheImgCode) || !cacheImgCode.equalsIgnoreCase(loginDTO.getVerifyCode())) {
            throw new QuickException(ResponseEnum.IMG_CODE_ERROR);
        }

        // 判断密码是否正确
        String encryptPwd = AESUtil.encrypt(loginDTO.getPassWord());
        if (!encryptPwd.equals(userPO.getPassword())) {
            throw new QuickException(ResponseEnum.PASSWORD_ERROR);
        }

        // 登录成功，登录状态保存到 Redis（保证某一时刻只有一个客户端登录）
        redisUtil.setCacheObject(loginDTO.getAccountId(), "登录状态占位");

        // 解析当前登录地址，切换用户状态【已上线】
        String location = IpUtil.getIpAddr(HttpServletUtil.getRequest());
        userPO.setLocation(location);
        userPO.setLoginStatus(YesNoEnum.YES.getCode());
        userStore.updateUserById(userPO);

//        // 通知已登录账号的客户端：您的账号在别处登录，是否是本人操作
//        Map<String, Object> param = new HashMap<>();
//        param.put("account_id", loginDTO.getAccountId());
//        param.put("location", location);
//        param.put("time", LocalDateTime.now());
//        kafkaProducer.send(KafkaConstant.SYSTEM_NOTICE_TOPIC, JSONUtil.toJsonStr(param));

        // 登录成功，返回 Token 和 账户信息
        Map<String, Object> result = new HashMap<>();
        String token = JwtUtil.generate(loginDTO.getAccountId());
        result.put("token", token);
        result.put("user", UserAdapter.buildUserVO(userPO));
        return result;
    }

    @Override
    public void captcha() throws IOException {
        // 封装响应信息
        HttpServletResponse response = HttpServletUtil.getResponse();
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
        redisUtil.setCacheObject(captcha, verifyCode, 3, TimeUnit.MINUTES);

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

    @Override
    public Boolean sendEmail(EmailDTO emailDTO) throws MessagingException, IOException {
        AbstractEmailStrategy emailStrategy = EmailStrategyFactory.getStrategyHandler(emailDTO.getType());
        return emailStrategy.sendEmail(emailDTO);
    }

    @Override
    public Boolean updateUser(UserUpdateDTO userDTO) {
        QuickChatUser userPO = UserAdapter.buildUserPO(userDTO);
        return userStore.updateUserById(userPO);
    }

    @Override
    public QuickChatUser getByToken() {
        String token = HttpServletUtil.getRequest().getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new QuickException(ResponseEnum.ACCOUNT_ID_NOT_EXIST);
        }
        Map<String, Object> resultMap = JwtUtil.resolve(token);
        String accountId = (String) resultMap.get(RequestContextUtil.ACCOUNT_ID);
        QuickChatUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.ACCOUNT_ID_NOT_EXIST);
        }
        userPO.setPassword(null);
        return userPO;
    }


    @Override
    public Boolean findBack(FindBackFormDTO findBackDTO) throws Exception {
        // 两次密码输入是否一致
        if (!findBackDTO.getPassword1().equals(findBackDTO.getPassword2())) {
            throw new QuickException(ResponseEnum.PASSWORD_DIFF);
        }

        // 判断账号是否存在
        QuickChatUser userPO = userStore.getByEmail(findBackDTO.getEmail());
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.EMAIL_NOT_REGISTERED);
        }

        // 判断邮箱验证码
        String cacheEmailCode = redisUtil.getCacheObject(RedisConstant.EMAIL_KEY + findBackDTO.getEmail());
        if (StringUtils.isEmpty(cacheEmailCode) || !cacheEmailCode.equals(findBackDTO.getEmailCode())) {
            throw new QuickException(ResponseEnum.EMAIL_CODE_ERROR);
        }

        // 更新密码
        String password = AESUtil.encrypt(findBackDTO.getPassword1());
        userPO.setPassword(password);
        return userStore.updateUserById(userPO);
    }
}
