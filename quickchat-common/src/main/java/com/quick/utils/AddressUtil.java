package com.quick.utils;

import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-01-04  09:59
 * @Description: ip2region 地址解析工具类
 * @Version: 1.0
 */
public class AddressUtil {
    private static final String TEMP_FILE_DIR = "/home/admin/app/";

    /**
     * 根据IP地址查询登录来源
     */
    public static String getCityInfo(String ip) {
        try {
            String dbPath = Objects.requireNonNull(AddressUtil.class.getResource("/ip2region/ip2region.xdb")).getPath();
            File file = new File(dbPath);
            //如果当前文件不存在，则从缓存中复制一份
            if (!file.exists()) {
                dbPath = TEMP_FILE_DIR + "ip.db";
                System.out.println(MessageFormat.format("当前目录为:[{0}]", dbPath));
                file = new File(dbPath);
                InputStream inputStream = AddressUtil.class.getClassLoader().getResourceAsStream("classpath:ip2region/ip2region.xdb");
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(inputStream), file);
            }
            Searcher searcher = Searcher.newWithFileOnly(dbPath);
            return searcher.searchByStr(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(getCityInfo("1.2.3.4"));
        System.out.println(getCityInfo("101.42.13.186"));
        System.out.println(getCityInfo("127.0.0.1"));
    }
}
