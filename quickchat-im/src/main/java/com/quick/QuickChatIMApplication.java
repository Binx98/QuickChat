package com.quick;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@Slf4j
@MapperScan("com.quick.mapper")
@SpringBootApplication
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
        log.info("----------------------------项目名称：[{}]----------------------------", applicationName);
        log.info("----------------------------项目环境：[{}]----------------------------", active);
        log.info("----------------------------项目端口：[{}]----------------------------", serverPort);
        log.info("----------------------------QuickChat快聊启动成功......----------------------------");
    }
}
