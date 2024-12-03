package com.quick.common.strategy.file.handler;

import com.quick.common.adapter.FileExtraAdapter;
import com.quick.common.enums.BucketEnum;
import com.quick.common.enums.ResponseEnum;
import com.quick.common.exception.QuickException;
import com.quick.common.pojo.dto.FileExtraDTO;
import com.quick.common.strategy.file.AbstractFileStrategy;
import com.quick.common.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
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
 * @Description: 语音上传
 */
@Slf4j
@Component
public class VoiceHandler extends AbstractFileStrategy {
    @Autowired
    private MinioUtil minioUtil;
    @Value("${quick-chat.size.voice}")
    private Integer fileSize;

    @Override
    protected BucketEnum getEnum() {
        return BucketEnum.VOICE;
    }

    @Override
    public Map<String, Object> uploadFile(MultipartFile file) throws Exception {
        // 文件大小校验
        long size = file.getSize() / 1024 / 1024;
        if (size > fileSize) {
            ResponseEnum responseEnum = ResponseEnum.FILE_OVER_SIZE;
            responseEnum.setMsg(String.format(responseEnum.getMsg(), fileSize + "MB"));
            throw new QuickException(responseEnum);
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

    @Override
    public void deleteFile(String url) throws Exception {
        minioUtil.delete(this.getEnum().getBucketName(), url);
    }
}
