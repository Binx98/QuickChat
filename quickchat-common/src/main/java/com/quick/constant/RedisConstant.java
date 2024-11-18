package com.quick.constant;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:19
 * @Description: Redis常量类
 * @Version: 1.0
 */
public class RedisConstant {
    /**
     * 缓存 Key
     */
    public static final String CAPTCHA_KEY = "captcha_key";
    public static final String EMAIL_KEY = "EMAIL_KEY:";
    public static final String PAGE_CURRENT_KEY = "PAGE_CURRENT_KEY:";


    /**
     * 缓存分区
     */
    public static final String QUICK_CHAT_USER = "quick_chat_user";
    public static final String QUICK_CHAT_FRIEND_CONTACT = "quick_chat_friend_contact";
    public static final String QUICK_CHAT_MSG = "quick_chat_msg";
    public static final String QUICK_CHAT_EMOJI = "quick_chat_emoji";
    public static final String QUICK_CHAT_SESSION = "quick_chat_session";
    public static final String QUICK_CHAT_GROUP = "quick_chat_group";
    public static final String QUICK_CHAT_GROUP_MEMBER = "quick_chat_group_member";
    public static final String QUICK_CHAT_APPLY = "quick_chat_apply";
}
