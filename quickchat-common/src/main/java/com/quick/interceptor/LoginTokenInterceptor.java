package com.quick.interceptor;

import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-20  18:06
 * @Description: 登录Token拦截器
 * @Version: 1.0
 */
@Slf4j
@Component
public class LoginTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 预请求直接通过
        String httpMethod = "OPTIONS";
        if (httpMethod.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 校验 Token
        String token = request.getHeader("token");
        if (!JwtUtil.check(token)) {
            throw new QuickException(ResponseEnum.TOKEN_EXPIRE);
        }

        // 校验通过
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
