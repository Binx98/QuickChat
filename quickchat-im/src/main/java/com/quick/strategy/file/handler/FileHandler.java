package com.quick.strategy.file.handler;

import com.quick.enums.BucketEnum;
import com.quick.strategy.file.AbstractFileStrategy;
import com.quick.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author 徐志斌
 * @Date: 2024/4/4 14:10
 * @Version 1.0
 * @Description: 文件上传
 */
@Component
public class FileHandler extends AbstractFileStrategy {
    @Autowired
    private MinioUtil minioUtil;

    @Override
    protected BucketEnum getEnum() {
        return BucketEnum.FILE;
    }

    @Override
    public Map<String, Object> uploadFile(MultipartFile file) throws Exception {
        // 文件大小校验
        String url = minioUtil.upload(file, this.getEnum().getBucketName());
        return null;
    }

    @Override
    public void downloadFile(String url) {
        minioUtil.download(this.getEnum().getBucketName(), url);
    }
}
