package com.quick.strategy.file.handler;

import com.quick.enums.BucketEnum;
import com.quick.strategy.file.AbstractFileStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 徐志斌
 * @Date: 2024/4/4 14:10
 * @Version 1.0
 * @Description: 头像上传
 */
@Component
public class AvatarHandler extends AbstractFileStrategy {

    @Override
    protected BucketEnum getEnum() {
        return BucketEnum.AVATAR;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        // 头像文件大小校验

        return null;
    }

    @Override
    public void downloadFile(String url) {

    }
}
