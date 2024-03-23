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
public enum BucketEnum {
    AVATAR(1, "avatar-bucket", "头像"),
    VOICE(2, "voice-bucket", "语音"),
    FILE(3, "file-bucket", "文件"),
    ;

    private Integer type;
    private String bucketName;
    private String desc;
}
