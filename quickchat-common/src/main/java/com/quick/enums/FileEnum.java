package com.quick.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-21  10:31
 * @Description: 文件类型枚举
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum FileEnum {
    AVATAR(1, "头像"),
    VOICE(2, "语音"),
    FILE(3, "文件（图片、视频）"),
    ;

    private Integer type;
    private String desc;
}
