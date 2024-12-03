package com.quick.common.sensitive;

import com.github.houbb.sensitive.word.api.IWordDeny;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-19  11:40
 * @Description: 自定义【敏感词】
 * @Version: 1.0
 */
@Slf4j
public class MyWordDeny implements IWordDeny {
    @Override
    public List<String> deny() {
        List<String> list = new ArrayList<>();
        try {
            Resource mySensitiveWords = new ClassPathResource("/src/main/resources/sensitive/mySensitiveWords.txt");
            Path mySensitiveWordsPath = Paths.get(mySensitiveWords.getFile().getPath());
            list = Files.readAllLines(mySensitiveWordsPath, StandardCharsets.UTF_8);
        } catch (IOException ioException) {
            log.error("读取敏感词文件错误！" + ioException.getMessage());
        }
        return list;
    }

}