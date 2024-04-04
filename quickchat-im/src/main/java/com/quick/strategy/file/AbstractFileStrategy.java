package com.quick.strategy.file;

import com.quick.enums.BucketEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-04-04  13:58
 * @Description: 上传文件策略抽象类
 * @Version: 1.0
 */
@Component
public abstract class AbstractFileStrategy {
    @PostConstruct
    private void initStrategyHandler() {
        FileStrategyFactory.register(this.getEnum().getCode(), this);
    }

    /**
     * 获取当前策略枚举
     *
     * @return Bucket桶枚举
     */
    protected abstract BucketEnum getEnum();

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件 url
     */
    public abstract String uploadFile(MultipartFile file);

    /**
     * 下载文件
     *
     * @param url 文件 url
     */
    public abstract void downloadFile(String url);
}
