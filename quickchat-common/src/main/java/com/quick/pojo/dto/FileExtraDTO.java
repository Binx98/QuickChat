package com.quick.pojo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @Author 徐志斌
 * @Date: 2024/3/3 20:00
 * @Version 1.0
 * @Description: 文件信息
 */
@Data
public class FileExtraDTO {
    /**
     * 文件名
     */
    @NotBlank(message = "文件名不能为空")
    private String name;

    /**
     * 文件大小：Byte
     */
    @Min(value = 0, message = "文件大小不能小于0")
    private Long size;

    /**
     * 文件类型
     */
    @NotBlank(message = "文件类型参数不能为空")
    private String type;

    /**
     * 语音文件时长
     */
    private Long voiceTime;
}
