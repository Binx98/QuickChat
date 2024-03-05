package com.quick.controller;

import com.quick.enums.FileEnum;
import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import com.quick.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 徐志斌
 * @Date: 2024/3/3 16:29
 * @Version 1.0
 * @Description: 文件控制器
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传文件
     *
     * @param type 文件类型
     * @param file 文件实体信息
     * @return 响应信息
     */
    @PostMapping("/upload/{type}")
    public R uploadFile(@PathVariable int type, MultipartFile file) throws Exception {
        String url = null;
        if (FileEnum.AVATAR.getType().equals(type)) {
            url = minioUtil.upload(file, FileEnum.AVATAR.getBucketName());
        } else if (FileEnum.VOICE.getType().equals(type)) {
            url = minioUtil.upload(file, FileEnum.VOICE.getBucketName());
        } else {
            url = minioUtil.upload(file, FileEnum.FILE.getBucketName());
        }
        return R.out(ResponseEnum.SUCCESS, url);
    }

    /**
     * 文件下载功能
     *
     * @param type     文件类型
     * @param fileName 文件名
     * @return 响应信息
     */
    @GetMapping("/download/{type}/{fileName}")
    public R downloadFile(@PathVariable int type, @PathVariable String fileName) {
        if (FileEnum.AVATAR.getType().equals(type)) {
            minioUtil.download(FileEnum.AVATAR.getBucketName(), fileName);
        } else if (FileEnum.VOICE.getType().equals(type)) {
            minioUtil.download(FileEnum.VOICE.getBucketName(), fileName);
        } else {
            minioUtil.download(FileEnum.FILE.getBucketName(), fileName);
        }
        return R.out(ResponseEnum.SUCCESS);
    }
}
