package com.salt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.salt.mapper")
@SpringBootApplication
public class SaltChatIMApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SaltChatIMApplication.class, args);
    }

    public void run(String... args) throws Exception {

    }
}
