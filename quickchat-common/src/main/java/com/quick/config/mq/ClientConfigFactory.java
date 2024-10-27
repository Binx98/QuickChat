package com.quick.config.mq;

import org.apache.rocketmq.client.ClientConfig;

/**
 * ClientConfig对象工厂类
 *
 * @author : Barry.chen
 * @date : 2024-10-19
 **/
public class ClientConfigFactory {

    /**
     * 创建 ClientConfig 对象
     *
     * @param namesrvAddr NameServer地址
     * @param instanceName 实例名称
     * @param clientIP 客户端IP
     * @return 配置好的ClientConfig对象
     */
    public static ClientConfig createClientConfig(String namesrvAddr, String instanceName, String clientIP) {
        ClientConfig clientConfig = new ClientConfig();

        // 设置NameServer地址
        clientConfig.setNamesrvAddr(namesrvAddr);

        // 设置客户端实例名称
        clientConfig.setInstanceName(instanceName);

        // 设置客户端IP
//        clientConfig.setClientIP(clientIP);

        // 可以继续设置其他配置...
        return clientConfig;
    }

}