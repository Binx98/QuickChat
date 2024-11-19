package com.quick.interceptor;

import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.utils.JwtUtil;
import com.quick.utils.RequestContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String httpMethod = "OPTIONS";
        if (httpMethod.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        String token = request.getHeader("token");
        if (!JwtUtil.check(token)) {
            throw new QuickException(ResponseEnum.TOKEN_EXPIRE);
        }

        Map<String, Object> tokenMap = JwtUtil.resolve(token);
        RequestContextUtil.setData(tokenMap);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RequestContextUtil.removeData();
    }
}
