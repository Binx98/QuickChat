package com.quick.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-07-05  13:39
 * @Description: 申请类型枚举
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum ApplyTypeEnum {
    FRIEND(1, "好友申请"),
    GROUP(2, "群聊申请");

    private Integer code;
    private String msg;
}
