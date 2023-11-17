package com.quick;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.quick.mapper")
@SpringBootApplication
public class QuickChatIMApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(QuickChatIMApplication.class, args);
    }

    public void run(String... args) throws Exception {

    }
}
