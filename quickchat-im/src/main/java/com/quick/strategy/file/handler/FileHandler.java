package com.quick.strategy.file.handler;

import com.quick.adapter.FileExtraAdapter;
import com.quick.enums.BucketEnum;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.pojo.dto.FileExtraDTO;
import com.quick.strategy.file.AbstractFileStrategy;
import com.quick.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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
    @Value("${quick-chat.size.file}")
    private Integer fileSize;

    @Override
    protected BucketEnum getEnum() {
        return BucketEnum.FILE;
    }

    @Override
    public Map<String, Object> uploadFile(MultipartFile file) throws Exception {
        // 文件大小校验
        long size = file.getSize() / 1024 / 1024;
        if (size > fileSize) {
            throw new QuickException(ResponseEnum.FILE_OVER_SIZE);
        }

        // 上传文件至 Minio
        String url = minioUtil.upload(file, this.getEnum().getBucketName());

        // 封装结果集
        FileExtraDTO extraDTO = FileExtraAdapter.buildFileExtraPO(file.getOriginalFilename(), file.getSize());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("url", url);
        resultMap.put("extraInfo", extraDTO);
        return resultMap;
    }

    @Override
    public void downloadFile(String url) {
        minioUtil.download(this.getEnum().getBucketName(), url);
    }
}
