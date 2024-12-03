package com.quick.common.exception;

import com.quick.common.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:19
 * @Version 1.0
 * @Description: 自定义通用异常
 */
@Getter
@AllArgsConstructor
public class QuickException extends RuntimeException {
    private ResponseEnum responseEnum;
}
