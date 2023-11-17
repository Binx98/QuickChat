package com.quick.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-08-29  13:43
 * -------------------------------------------------------
 * 不要携带yml配置文件统一path路径，否则失效（例如：/user/captcha）
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
//    /**
//     * 拦截器配置
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        final List<String> EXCLUDE_PATH = Arrays.asList(
//                "/login",
//                "/register",
//                "/resolve_token",
//                "/captcha",
//                "/list_by_ids",
//                "/test",
//                "/chat/search_ws_connect"
//        );
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(EXCLUDE_PATH);
//    }


    /**
     * 前后端跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(1800)
                .allowedOrigins("*");
    }
}
