package com.quick;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@MapperScan("com.quick.mapper.*")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class QuickChatIMApplication implements CommandLineRunner {
    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(QuickChatIMApplication.class, args);
    }

    public void run(String... args) {
        String applicationName = environment.getProperty("spring.application.name");
        String serverPort = environment.getProperty("server.port");
        String active = environment.getProperty("spring.profiles.active");
        System.setProperty("rocketmq.client.logUseSlf4j", "true");
        log.info("----------------------------项目名称：[{}]----------------------------", applicationName);
        log.info("----------------------------项目环境：[{}]----------------------------", active);
        log.info("----------------------------项目端口：[{}]----------------------------", serverPort);
        log.info("----------------------------QuickChat启动成功！----------------------------");
    }
}
