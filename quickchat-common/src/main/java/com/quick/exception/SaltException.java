package com.quick.exception;

import com.quick.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:19
 * @Version 1.0
 * @Description: 自定义异常
 */
@Getter
@AllArgsConstructor
public class SaltException extends RuntimeException {
    private ResponseEnum responseEnum;
}
