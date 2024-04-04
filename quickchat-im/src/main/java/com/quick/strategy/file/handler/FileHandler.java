package com.quick.strategy.file.handler;

import com.quick.enums.BucketEnum;
import com.quick.strategy.file.AbstractFileStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 徐志斌
 * @Date: 2024/4/4 14:10
 * @Version 1.0
 * @Description: 文件上传
 */
@Component
public class FileHandler  extends AbstractFileStrategy {
    @Override
    protected BucketEnum getEnum() {
        return BucketEnum.FILE;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return null;
    }

    @Override
    public void downloadFile(String url) {

    }
}
