package com.quick.utils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.minio.*;
import io.minio.messages.Item;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2023/7/23 18:29
 * @Version 1.0
 * @Description: MinIO工具类
 */
@Slf4j
@Component
public class MinioUtil {
    @Value("${minio.endpoint}")
    private String endPoint;
    @Autowired
    private MinioClient minioClient;

    /**
     * 上传文件
     *
     * @param file       文件对象
     * @param bucketName Bucket桶名
     * @return 文件url
     */
    public String upload(MultipartFile file, String bucketName) throws Exception {
        String fileName = file.getOriginalFilename();
        String[] split = fileName.split("\\.");
        if (split.length > 1) {
            fileName = split[0] + "_" + IdWorker.getId() + "." + split[1];
        } else {
            fileName = fileName + IdWorker.getId();
        }
        InputStream in = null;
        try {
            in = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(in, in.available(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(putObjectArgs);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        StringBuffer url = new StringBuffer();
        url.append(endPoint);
        url.append("/");
        url.append(bucketName);
        url.append("/");
        url.append(fileName);
        return url.toString();
    }

    /**
     * 下载文件
     *
     * @param bucketName Bucket桶名
     * @param url        文件url
     */
    public void download(String bucketName, String url) {
        String filename = this.getFileNameFromURL(url);
        try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build())) {
            HttpServletResponse response = HttpServletUtil.getResponse();
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition",
                    "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.close();
        } catch (Exception e) {
            log.error("file download from minio exception, file name: {}", filename, e);
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName Bucket桶名
     * @param fileName   文件名
     */
    public void delete(String bucketName, String fileName) throws Exception {
        RemoveObjectArgs removeArgs = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        minioClient.removeObject(removeArgs);
    }

    /**
     * 根据 url 获取文件名称
     *
     * @param url 文件url
     * @return 文件名
     */
    private String getFileNameFromURL(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            return url;
        }
        return url.substring(lastSlashIndex + 1);
    }


    /**
     * 创建 bucket
     */
    public void createBucket(String bucketName) throws Exception {
        minioClient.makeBucket(
                MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
    }

    /**
     * 删除 bucket
     */
    public void deleteBucket(String bucketName) throws Exception {
        minioClient.removeBucket(
                RemoveBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
    }

    /**
     * 判断 bucket 是否存在（不存在则创建）
     */
    public void existBucket(String name) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
        if (!exists) {
            this.createBucket(name);
        }
    }

    /**
     * 查看文件列表
     */
    public List<Item> getFileList(String bucketName) throws Exception {
        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        List<Item> objectItems = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            objectItems.add(item);
        }
        return objectItems;
    }
}
