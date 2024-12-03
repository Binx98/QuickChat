//package com.quick.common.config;
//
//import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Author: 徐志斌
// * @CreateTime: 2024-11-01  15:15
// * @Description: XXL-JOB 配置类
// * @Version: 1.0
// */
//@Configuration
//public class XxlJobConfig {
//    @Value("${xxl.job.admin.addresses}")
//    private String adminAddresses;
//    @Value("${xxl.job.accessToken}")
//    private String accessToken;
//    @Value("${xxl.job.executor.appname}")
//    private String appname;
//    @Value("${xxl.job.executor.address}")
//    private String address;
//    @Value("${xxl.job.executor.ip}")
//    private String ip;
//    @Value("${xxl.job.executor.port}")
//    private int port;
//    @Value("${xxl.job.executor.logpath}")
//    private String logPath;
//    @Value("${xxl.job.executor.logretentiondays}")
//    private int logRetentionDays;
//
//    @Bean
//    public XxlJobSpringExecutor xxlJobExecutor() {
//        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
//        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
//        xxlJobSpringExecutor.setAppname(appname);
//        xxlJobSpringExecutor.setAddress(address);
//        xxlJobSpringExecutor.setIp(ip);
//        xxlJobSpringExecutor.setPort(port);
//        xxlJobSpringExecutor.setAccessToken(accessToken);
//        xxlJobSpringExecutor.setLogPath(logPath);
//        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
//        return xxlJobSpringExecutor;
//    }
//}