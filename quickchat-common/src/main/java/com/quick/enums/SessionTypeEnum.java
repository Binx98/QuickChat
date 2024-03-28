package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-01-09  09:57
 * @Description: 会话类型枚举
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum SessionTypeEnum {
    SINGLE(1, "单聊"),
    GROUP(2, "群聊");

    private Integer code;
    private String msg;
}
