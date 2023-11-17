package com.quick.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author 徐志斌
 * @Date: 2023/8/6 11:11
 * @Version 1.0
 * @Description: ip2region：https://github.com/lionsoul2014/ip2region
 */
@Slf4j
public class IPUtil {
    /**
     * 获取ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress != null && ipAddress.length() != 0 && !"unknown".equalsIgnoreCase(ipAddress)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                if (ipAddress.indexOf(",") != -1) {
                    ipAddress = ipAddress.split(",")[0];
                }
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("========================ip2region解析公网ip地址失败：[{e}] ========================", e);
        }
        return ipAddress;
    }

    /**
     * 根据 IP 查询登录来源
     */
    public static Map<String, String> getLocation(String ip) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        String dbPath = Objects.requireNonNull(IPUtil.class.getResource("/ip2region/ip2region.xdb")).getPath();
        Searcher searcher = Searcher.newWithFileOnly(dbPath);
        String cityInfo = searcher.searchByStr(ip);
        if (StringUtils.isNotEmpty(cityInfo)) {
            String[] infoArr = cityInfo.split("\\|");
            String country = infoArr[0];
            String province = infoArr[2];
            String city = infoArr[3];
            resultMap.put("country", country);
            resultMap.put("province", province);
            resultMap.put("city", city);
            return resultMap;
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
//        Map<String, String> location = getLocation("101.42.13.186");
        Map<String, String> location = getLocation("127.0.0.1");
        System.out.println(location);
    }
}

