package com.quick.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 徐志斌
 * @Date: 2024/3/3 20:00
 * @Version 1.0
 * @Description: 文件信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileExtraDTO {
    /**
     * 文件名
     */
    private String name;

    /**
     * 文件大小：Byte
     */
    private Long size;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 语音文件时长
     */
    private Long voiceTime;
}
