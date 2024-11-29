package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.quick.adapter.GroupMemberAdapter;
import com.quick.adapter.SessionAdapter;
import com.quick.adapter.UserAdapter;
import com.quick.constant.RedisConstant;
import com.quick.enums.ResponseEnum;
import com.quick.enums.SessionTypeEnum;
import com.quick.enums.YesNoEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickChatUserMapper;
import com.quick.pojo.dto.*;
import com.quick.pojo.po.QuickChatGroupMember;
import com.quick.pojo.po.QuickChatSession;
import com.quick.pojo.po.QuickChatUser;
import com.quick.pojo.vo.ChatUserVO;
import com.quick.service.QuickUserService;
import com.quick.store.mysql.QuickChatGroupMemberStore;
import com.quick.store.mysql.QuickChatSessionStore;
import com.quick.store.mysql.QuickChatUserStore;
import com.quick.strategy.email.AbstractEmailStrategy;
import com.quick.strategy.email.EmailStrategyFactory;
import com.quick.utils.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
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
    private QuickChatUserStore userStore;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private QuickChatSessionStore sessionStore;
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;
    @Autowired
    private QuickChatGroupMemberStore memberStore;
    @Value("${quick-chat.common-group-id}")
    private Long officialGroupId;

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
    public void register(RegisterFormDTO registerDTO) throws Exception {
        if (!registerDTO.getPassword1().equals(registerDTO.getPassword2())) {
            throw new QuickException(ResponseEnum.PASSWORD_DIFF);
        }
        String cacheEmailCode = redisUtil.getCacheObject(RedisConstant.EMAIL_KEY + registerDTO.getEmail());
        if (StringUtils.isEmpty(cacheEmailCode) || !registerDTO.getEmailCode().equalsIgnoreCase(cacheEmailCode)) {
            throw new QuickException(ResponseEnum.EMAIL_CODE_ERROR);
        }
        QuickChatUser userPO = userStore.getByAccountId(registerDTO.getAccountId());
        if (ObjectUtils.isNotEmpty(userPO)) {
            throw new QuickException(ResponseEnum.ACCOUNT_ID_EXIST);
        }
        if (registerDTO.getEmail().equals(userPO.getEmail())) {
            throw new QuickException(ResponseEnum.EMAIL_HAS_REGISTERED);
        }
        if (sensitiveWordUtil.check(registerDTO.getNickName())) {
            throw new QuickException(ResponseEnum.NICK_NAME_NOT_ALLOW);
        }
        QuickChatGroupMember memberPO = GroupMemberAdapter.buildMemberPO(officialGroupId, registerDTO.getAccountId());
        memberStore.saveMember(memberPO);
        QuickChatSession chatSession = SessionAdapter.buildSessionPO(registerDTO.getAccountId(),
                officialGroupId.toString(), officialGroupId, SessionTypeEnum.GROUP.getCode());
        sessionStore.saveInfo(chatSession);
        String location = IpUtil.getIpAddr(HttpServletUtil.getRequest());
        String password = AESUtil.encrypt(registerDTO.getPassword1());
        userPO = UserAdapter.buildUserPO(registerDTO.getAccountId(), null, password,
                registerDTO.getGender(), registerDTO.getEmail(), location, YesNoEnum.NO.getCode());
        userStore.saveUser(userPO);
    }

    @Override
    public Map<String, Object> login(LoginFormDTO loginDTO) throws Exception {
        QuickChatUser userPO = userStore.getByAccountId(loginDTO.getAccountId());
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.ACCOUNT_ID_NOT_EXIST);
        }
        String captchaKey = HttpServletUtil.getRequest().getHeader(RedisConstant.CAPTCHA_KEY);
        String cacheImgCode = redisUtil.getCacheObject(captchaKey);
        if (StringUtils.isEmpty(cacheImgCode) || !cacheImgCode.equalsIgnoreCase(loginDTO.getVerifyCode())) {
            throw new QuickException(ResponseEnum.IMG_CODE_ERROR);
        }
        String encryptPwd = AESUtil.encrypt(loginDTO.getPassWord());
        if (!encryptPwd.equals(userPO.getPassword())) {
            throw new QuickException(ResponseEnum.PASSWORD_ERROR);
        }
        redisUtil.setCacheObject(loginDTO.getAccountId(), "login flag");
        String location = IpUtil.getIpAddr(HttpServletUtil.getRequest());
        userPO.setLocation(location);
        userPO.setLoginStatus(YesNoEnum.YES.getCode());
        userStore.updateUserById(userPO);
        Map<String, Object> result = new HashMap<>();
        String token = JwtUtil.generate(loginDTO.getAccountId());
        result.put("token", token);
        result.put("user", UserAdapter.buildUserVO(userPO));
        return result;
    }

    @Override
    public void captcha() throws IOException {
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String captcha = RedisConstant.CAPTCHA_KEY + ":" + UUID.randomUUID();
        CookieUtil.addCookie(response, RedisConstant.CAPTCHA_KEY, captcha, false, -1, "/");
        String verifyCode = defaultKaptcha.createText();
        redisUtil.setCacheObject(captcha, verifyCode, 3, TimeUnit.MINUTES);
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
    public void sendEmail(EmailDTO emailDTO) throws IOException, MessagingException {
        AbstractEmailStrategy emailStrategy = EmailStrategyFactory.getStrategyHandler(emailDTO.getType());
        emailStrategy.sendEmail(emailDTO);
    }

    @Override
    public void updateUser(UserInfoDTO userDTO) {
        QuickChatUser userPO = UserAdapter.buildUserPO(
                userDTO.getAccountId(),
                userDTO.getNickName(),
                userDTO.getAvatar(),
                userDTO.getGender()
        );
        userStore.updateUserById(userPO);
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
    public void findBack(FindBackFormDTO findBackDTO) throws Exception {
        if (!findBackDTO.getPassword1().equals(findBackDTO.getPassword2())) {
            throw new QuickException(ResponseEnum.PASSWORD_DIFF);
        }
        QuickChatUser userPO = userStore.getByEmail(findBackDTO.getEmail());
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.EMAIL_NOT_REGISTERED);
        }
        String cacheEmailCode = redisUtil.getCacheObject(RedisConstant.EMAIL_KEY + findBackDTO.getEmail());
        if (StringUtils.isEmpty(cacheEmailCode) || !cacheEmailCode.equals(findBackDTO.getEmailCode())) {
            throw new QuickException(ResponseEnum.EMAIL_CODE_ERROR);
        }
        String encryptPassword = AESUtil.encrypt(findBackDTO.getPassword1());
        userPO.setPassword(encryptPassword);
        userStore.updateUserById(userPO);
    }
}
