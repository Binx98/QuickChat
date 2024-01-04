package com.quick.pojo.entity;

import lombok.Data;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-01-04  09:21
 * @Description: 地址实体
 * @Version: 1.0
 */
@Data
public class IpInfo {
    /**
     * 国家
     */
    private String country;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String region;
    /**
     * 运营商
     */
    private String isp;
}