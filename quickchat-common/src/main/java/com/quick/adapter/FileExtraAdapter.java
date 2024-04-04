package com.quick.adapter;

import com.quick.pojo.dto.FileExtraDTO;

/**
 * @Author 徐志斌
 * @Date: 2024/4/4 15:42
 * @Version 1.0
 * @Description: 文件额外信息
 */
public class FileExtraAdapter {

    public static FileExtraDTO buildFileExtraPO(String fileName, long size) {
        return FileExtraDTO.builder()
                .name(fileName)
                .size(size)
                .build();
    }
}
