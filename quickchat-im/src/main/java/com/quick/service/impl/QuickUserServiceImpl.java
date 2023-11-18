package com.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.quick.adapter.UserAdapter;
import com.quick.constant.RedisConstant;
import com.quick.enums.LineEnum;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.mapper.QuickUserMapper;
import com.quick.pojo.dto.LoginDTO;
import com.quick.pojo.dto.RegisterDTO;
import com.quick.pojo.po.QuickUser;
import com.quick.pojo.vo.UserVO;
import com.quick.service.QuickUserService;
import com.quick.store.QuickUserStore;
import com.quick.utils.CookieUtil;
import com.quick.utils.IPUtil;
import com.quick.utils.RedisUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
public class QuickUserServiceImpl extends ServiceImpl<QuickUserMapper, QuickUser> implements QuickUserService {
    @Autowired
    private QuickUserStore userStore;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据 account_id 查询用户信息
     */
    @Override
    public UserVO getByAccountId(String accountId) {
        QuickUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isEmpty(userPO)) {
            throw new QuickException(ResponseEnum.USER_NOT_EXIST);
        }
        return UserAdapter.buildUserVO(userPO);
    }

    /**
     * 注册账号
     */
    @Override
    public Boolean register(RegisterDTO registerDTO, HttpServletRequest request) throws Exception {
        String accountId = registerDTO.getAccountId();
        String nickName = registerDTO.getNickName();
        String password1 = registerDTO.getPassword1();
        String password2 = registerDTO.getPassword2();
        Integer gender = registerDTO.getGender();
        String email = registerDTO.getEmail();
        String verifyCode = registerDTO.getVerifyCode();

        // 两次密码输入是否一致
        if (!password1.equals(password2)) {
            throw new QuickException(ResponseEnum.PASSWORD_DIFF);
        }

        // 判断图片验证码是否失效
        String captchaKey = request.getHeader("captcha_key");
        String cacheVerifyCode = redisUtil.getCacheObject(captchaKey);
        if (StringUtils.isEmpty(cacheVerifyCode)) {
            throw new QuickException(ResponseEnum.VERIFY_CODE_EXPIRE);
        }

        // 判断验证码输入是否正确
        if (!verifyCode.equalsIgnoreCase(cacheVerifyCode)) {
            throw new QuickException(ResponseEnum.VERIFY_CODE_ERROR);
        }

        // 判断账号是否存在
        QuickUser userPO = userStore.getByAccountId(accountId);
        if (ObjectUtils.isNotEmpty(userPO)) {
            throw new QuickException(ResponseEnum.ACCOUNT_ID_EXIST);
        }

        // 解析地址
        String ipAddress = IPUtil.getIpAddress(request);
        // TODO 判断如果是本机ip，就不处理
        Map<String, String> locationMap = IPUtil.getLocation(ipAddress);
        String location = locationMap.get("province") + "-" + locationMap.get("city");

        // 保存账号信息
        userPO = UserAdapter.buildUserPO(accountId, nickName, password1, gender, email, location, LineEnum.OFFLINE.getType());
        return userStore.saveUserInfo(userPO);
    }

    /**
     * 登陆账号
     */
    @Override
    public Boolean login(LoginDTO loginDTO, HttpServletRequest request) {
        return null;
    }

    /**
     * 生成验证码
     */
    @Override
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        // 生成验证码图片、内容
        String verifyCode = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(verifyCode);

        // 没有CookieKey，赋值Cookie和Redis、有重新赋值
        String uuid = null;
        if (!CookieUtil.hasCookie(request, RedisConstant.COOKIE_KEY)) {
            uuid = UUID.randomUUID().toString();
            CookieUtil.addCookie(response, RedisConstant.COOKIE_KEY, uuid, false, -1, "/");
        } else {
            uuid = CookieUtil.getValue(request, RedisConstant.COOKIE_KEY);
        }

        // 验证码缓存到Redis（10min）
        redisUtil.setCacheObject(uuid, verifyCode, 10, TimeUnit.MINUTES);

        // 将图片输出到页面
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
