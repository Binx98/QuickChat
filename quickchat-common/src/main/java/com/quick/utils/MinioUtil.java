package com.quick.utils;

import io.minio.*;
import io.minio.messages.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2023/7/23 18:29
 * @Version 1.0
 * @Description: MinIO工具类
 */
@Component
public class MinioUtil {
    @Value("${minio.endpoint}")
    private String endPoint;
    @Autowired
    private MinioClient minioClient;

    /**
     * 上传文件
     */
    public String upload(MultipartFile file, String bucketName) throws Exception {
        String fileName = file.getOriginalFilename();
        String[] split = fileName.split("\\.");
        if (split.length > 1) {
            fileName = split[0] + "_" + System.currentTimeMillis() + "." + split[1];
        } else {
            fileName = fileName + System.currentTimeMillis();
        }
        InputStream in = null;
        try {
            in = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(in, in.available(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
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
     */
    public void download(String bucketName, String url) throws Exception {
        HttpServletResponse response = HttpServletUtil.getResponse();
        if (StringUtils.isBlank(url)) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String data = "文件下载失败";
            OutputStream ps = response.getOutputStream();
            ps.write(data.getBytes("UTF-8"));
            return;
        }
        try {
            // 获取文件对象
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(this.getFileNameFromURL(url))
                    .build();
            InputStream object = minioClient.getObject(args);
            byte buf[] = new byte[1024];
            int length = 0;
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(this.getFileNameFromURL(url), "UTF-8"));
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            OutputStream outputStream = response.getOutputStream();
            // 输出文件
            while ((length = object.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            // 关闭输出流
            outputStream.close();
        } catch (Exception ex) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String data = "文件下载失败";
            OutputStream ps = response.getOutputStream();
            ps.write(data.getBytes("UTF-8"));
        }
    }

    private String getFileNameFromURL(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            return url;
        }
        return url.substring(lastSlashIndex + 1);
    }

    /**
     * 查看文件对象
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

    /**
     * 删除文件对象
     */
    public void removeObject(String bucketName, String objName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objName).build());
    }

    /**
     * 创建 bucket
     */
    public void createBucket(String bucketName) throws Exception {
        minioClient.makeBucket(MakeBucketArgs.builder()
                .bucket(bucketName)
                .build());
    }

    /**
     * 删除 bucket
     */
    public void deleteBucket(String bucketName) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build());
    }

    /**
     * 判断bucket是否存在（不存在则创建）
     */
    public void existBucket(String name) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
        }
    }
}
