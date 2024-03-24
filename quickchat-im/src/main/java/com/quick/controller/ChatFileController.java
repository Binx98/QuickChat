package com.quick.controller;

import com.quick.enums.BucketEnum;
import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import com.quick.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 徐志斌
 * @Date: 2024/3/3 16:29
 * @Version 1.0
 * @Description: 文件控制器
 */
@Controller
@RequestMapping("/file")
public class ChatFileController {
    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传文件
     *
     * @param type 文件类型
     * @param file 文件实体信息
     * @return 响应信息
     */
    @ResponseBody
    @PostMapping("/upload/{type}")
    public R uploadFile(@PathVariable int type, MultipartFile file) throws Exception {
        String url = null;
        if (BucketEnum.AVATAR.getType().equals(type)) {
            url = minioUtil.upload(file, BucketEnum.AVATAR.getBucketName());
        } else if (BucketEnum.VOICE.getType().equals(type)) {
            url = minioUtil.upload(file, BucketEnum.VOICE.getBucketName());
        } else {
            url = minioUtil.upload(file, BucketEnum.FILE.getBucketName());
        }
        return R.out(ResponseEnum.SUCCESS, url);
    }

    /**
     * 文件下载功能
     *
     * @param type 文件类型
     * @param url  文件url
     * @return 响应信息
     */
    @GetMapping("/download/{type}")
    public void downloadFile(@PathVariable int type, String url) {
        if (BucketEnum.AVATAR.getType().equals(type)) {
            minioUtil.download(BucketEnum.AVATAR.getBucketName(), url);
        } else if (BucketEnum.VOICE.getType().equals(type)) {
            minioUtil.download(BucketEnum.VOICE.getBucketName(), url);
        } else {
            minioUtil.download(BucketEnum.FILE.getBucketName(), url);
        }
    }
}
