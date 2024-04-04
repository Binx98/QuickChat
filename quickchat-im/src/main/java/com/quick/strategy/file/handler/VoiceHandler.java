package com.quick.strategy.file.handler;

import com.quick.enums.BucketEnum;
import com.quick.pojo.dto.FileExtraDTO;
import com.quick.strategy.file.AbstractFileStrategy;
import com.quick.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 徐志斌
 * @Date: 2024/4/4 14:10
 * @Version 1.0
 * @Description: 语音上传
 */
@Slf4j
@Component
public class VoiceHandler extends AbstractFileStrategy {
    @Autowired
    private MinioUtil minioUtil;

    @Override
    protected BucketEnum getEnum() {
        return BucketEnum.VOICE;
    }

    @Override
    public Map<String, Object> uploadFile(MultipartFile file) throws Exception {
        // 文件大小校验


        // 上传文件至 Minio
        String url = minioUtil.upload(file, this.getEnum().getBucketName());

        // 封装结果集
        FileExtraDTO extraDTO = FileExtraDTO.builder()
                .name(file.getOriginalFilename())
                .size(file.getSize())
                .build();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("url", url);
        resultMap.put("extraInfo", extraDTO);
        return null;
    }

    @Override
    public void downloadFile(String url) {
        minioUtil.download(this.getEnum().getBucketName(), url);
    }
}
