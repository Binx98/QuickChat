package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.response.R;
import com.quick.service.QuickChatFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "文件相关")
@Controller
@RequestMapping("/file")
public class QuickChatFileController {
    @Autowired
    private QuickChatFileService fileService;

    @ApiOperation("上传文件")
    @ResponseBody
    @PostMapping("/upload/{type}")
    public R uploadFile(@PathVariable Integer type, MultipartFile file) throws Exception {
        Map<String, Object> resultMap = fileService.uploadFile(type, file);
        return R.out(ResponseEnum.SUCCESS, resultMap);
    }

    @ApiOperation("下载文件")
    @GetMapping("/download/{type}")
    public void downloadFile(@PathVariable Integer type, String url) {
        fileService.downloadFile(type, url);
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/delete/{type}")
    public void deleteFile(@PathVariable Integer type, String url) throws Exception {
        fileService.deleteFile(type, url);
    }
}
