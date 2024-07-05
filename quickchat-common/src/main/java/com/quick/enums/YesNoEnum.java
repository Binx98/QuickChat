package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 徐志斌
 * @Date: 2023/11/25 13:13
 * @Version 1.0
 * @Description: 判断枚举
 */
@Getter
@AllArgsConstructor
public enum YesNoEnum {
    YES(0, "是"),
    NO(1, "否");

    private Integer code;
    private String msg;
}
