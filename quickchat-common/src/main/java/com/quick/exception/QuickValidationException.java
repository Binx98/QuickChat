package com.quick.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.BindException;

/**
 * @Author 陈辰
 * @Date: 2024/7/10 20:27
 * @Version 1.0
 * @Description: 自定义注解校验异常
 */
@Getter
@AllArgsConstructor
public class QuickValidationException extends BindException {

    private String errorMsg;

}
