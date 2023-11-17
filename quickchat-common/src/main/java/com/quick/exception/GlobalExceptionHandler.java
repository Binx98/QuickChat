package com.quick.exception;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author 徐志斌
 * @Date: 2023/6/30 21:22
 * @Version 1.0
 * @Description: 全局统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 自定义异常 QuickException
     */
    @ExceptionHandler(QuickException.class)
    public R bingoException(QuickException e) {
        return R.out(e.getResponseEnum());
    }

    /**
     * 所有异常
     */
    @ExceptionHandler(Exception.class)
    public R bindException(Exception e) {
        log.error("========================Exception：{}========================", e);
        return R.out(ResponseEnum.FAIL, e);
    }
}
