package com.quick.common.adapter;

import com.quick.common.pojo.dto.FileExtraDTO;

/**
 * @Author 徐志斌
 * @Date: 2024/4/4 15:42
 * @Version 1.0
 * @Description: 文件信息适配器
 */
public class FileExtraAdapter {

    public static FileExtraDTO buildFileExtraPO(String fileName, long size) {
        FileExtraDTO extraDTO = new FileExtraDTO();
        extraDTO.setName(fileName);
        extraDTO.setSize(size);
        return extraDTO;
    }
}
