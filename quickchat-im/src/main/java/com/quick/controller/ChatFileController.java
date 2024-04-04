package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import com.quick.service.QuickChatFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
    private QuickChatFileService fileService;

    /**
     * 上传文件
     *
     * @param type Bucket类型
     * @param file 文件实体信息
     */
    @ResponseBody
    @PostMapping("/upload/{type}")
    public R uploadFile(@PathVariable int type, MultipartFile file) throws Exception {
        Map<String, Object> resultMap = fileService.uploadFile(type, file);
        return R.out(ResponseEnum.SUCCESS, resultMap);
    }

    /**
     * 文件下载功能
     *
     * @param type Bucket类型
     * @param url  文件url
     */
    @GetMapping("/download/{type}")
    public void downloadFile(@PathVariable int type, String url) {
        fileService.downloadFile(type, url);
    }
}
