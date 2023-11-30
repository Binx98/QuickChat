package com.quick.controller;

import com.quick.enums.FileEnum;
import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-21  10:28
 * @Description: 文件操作
 * @Version: 1.0
 */
@RestController
@RequestMapping("/file")
public class ChatFileController {
    /**
     * 上传文件
     *
     * @see FileEnum
     */
    @PostMapping("/upload")
    public R uploadFile(MultipartFile file) {
        return R.out(ResponseEnum.SUCCESS);
    }
}
