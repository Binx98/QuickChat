package com.quick.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 表情包 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-04-04
 */
public interface QuickChatFileService {
    /**
     * 上传文件
     *
     * @param type Bucket类型
     * @param file 文件对象
     * @return 文件访问url
     */
    String uploadFile(int type, MultipartFile file) throws Exception;

    /**
     * 下载文件
     *
     * @param type Bucket类型
     * @param url  文件url
     * @return 执行结果
     */
    Boolean downloadFile(int type, String url);
}
