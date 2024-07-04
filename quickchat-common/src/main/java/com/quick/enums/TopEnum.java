package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-07-04  14:34
 * @Description: 会话置顶枚举
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum TopEnum {
    NO(0, "未置顶"),
    YES(1, "置顶");

    private Integer code;
    private String msg;
}
