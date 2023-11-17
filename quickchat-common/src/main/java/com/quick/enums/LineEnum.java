package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  14:46
 * @Description: 账户在线状态
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum LineEnum {
    ONLINE(1, "在线"),
    OFFLINE(2, "离线");

    private Integer type;
    private String desc;
}
