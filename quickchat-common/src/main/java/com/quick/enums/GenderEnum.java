package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:37
 * @Description: 性别枚举
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {
    GIRL(0, "女"),
    BOY(1, "男");

    private Integer type;
    private String desc;
}
