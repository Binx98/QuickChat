package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  14:46
 * @Description: 邮件类型枚举
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum EmailEnum {
    VERIFY_CODE(1, "验证码"),
    ;

    private Integer type;
    private String desc;
}
