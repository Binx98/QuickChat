package com.quick.exception;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.validation.BindException;
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
    public R quickException(QuickException e) {
        log.error("========================QuickException：{}========================", e);
        return R.out(e.getResponseEnum());
    }

    /**
     * 注解校验异常 BindException
     */
    @ExceptionHandler(BindException.class)
    public R validationException(BindException b) {
        log.error("========================BindException：{}========================", b);
        final StringBuilder sb = new StringBuilder();
        b.getBindingResult().getAllErrors().forEach(e -> sb.append(e.getDefaultMessage()).append("\r\n"));
        return R.out(ResponseEnum.FAIL, sb);
    }

    /**
     * 自定义注解校验异常 QuickValidationException
     */
    @ExceptionHandler({QuickValidationException.class})
    public R quickValidationException(QuickValidationException e) {
        log.error("========================QuickValidationException：{}========================", e);
        return R.out(ResponseEnum.FAIL, ExceptionUtils.getMessage(e));
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
