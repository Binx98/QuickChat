package com.quick.constant;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:19
 * @Description: Redis常量类
 * @Version: 1.0
 */
public class RedisConstant {
    /**
     * Cookie Key
     */
    public static final String CAPTCHA_KEY = "captcha_key";

    /**
     * 分布式锁KEY
     */
    public static final String UNREAD_LOCK_KEY = "UNREAD_LOCK_KEY:";

    /**
     * 缓存分区
     */
    public static final String QUICK_USER = "quick_user";
    public static final String QUICK_CHAT_MSG = "quick_chat_msg";
    public static final String QUICK_CHAT_EMOJI = "quick_chat_emoji";
    public static final String QUICK_CHAT_SESSION = "quick_chat_session";
    public static final String QUICK_CHAT_GROUP_MEMBER = "quick_chat_group_member";

    /**
     * 其他
     */
    public static final String EMAIL_KEY = "EMAIL_KEY:";
}
