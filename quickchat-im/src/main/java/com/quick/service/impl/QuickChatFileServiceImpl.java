package com.quick.service.impl;

import com.quick.enums.BucketEnum;
import com.quick.service.QuickChatFileService;
import com.quick.strategy.file.AbstractFileStrategy;
import com.quick.strategy.file.FileStrategyFactory;
import com.quick.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 表情包 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-04-04
 */
@Service
public class QuickChatFileServiceImpl implements QuickChatFileService {
    @Autowired
    private MinioUtil minioUtil;

    @Override
    public String uploadFile(int type, MultipartFile file) throws Exception {
        AbstractFileStrategy fileStrategy = FileStrategyFactory.getStrategyHandler(type);
        fileStrategy.uploadFile(file);

        String url = null;
        if (BucketEnum.AVATAR.getCode().equals(type)) {
            url = minioUtil.upload(file, BucketEnum.AVATAR.getBucketName());
        } else if (BucketEnum.VOICE.getCode().equals(type)) {
            url = minioUtil.upload(file, BucketEnum.VOICE.getBucketName());
        } else {
            url = minioUtil.upload(file, BucketEnum.FILE.getBucketName());
        }
        return url;
    }

    @Override
    public Boolean downloadFile(int type, String url) {
        AbstractFileStrategy fileStrategy = FileStrategyFactory.getStrategyHandler(type);
        fileStrategy.downloadFile(url);
        return null;
    }
}
