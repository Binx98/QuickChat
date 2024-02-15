package com.quick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @Author 徐志斌
 * @Date: 2024/02/14 20:42:33
 * @Version 1.0
 * @Description: Redis Lua脚本配置类
 */
@Configuration
public class LuaScriptConfig {
    public static final String RATE_LIMIT_LUA = "RATE_LIMIT_LUA";

    /**
     * RateLimiter.lua 限流脚本
     */
    @Bean(RATE_LIMIT_LUA)
    public DefaultRedisScript<Long> rateLimitScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/RateLimit.lua")));
        script.setResultType(Long.class);
        return script;
    }
}
