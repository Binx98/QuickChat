package com.quick.utils;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-19  11:40
 * @Description: 敏感词过滤工具类
 * @Version: 1.0
 */
@Component
public class SensitiveWordUtil {
    @Autowired
    private SensitiveWordBs sensitiveWordBs;

    /**
     * 刷新敏感词库与非敏感词库缓存
     */
    public void refresh() {
        sensitiveWordBs.init();
    }

    /**
     * 判断是否含有敏感词
     */
    public boolean check(String text) {
        return sensitiveWordBs.contains(text);
    }

    /**
     * 指定替换符进行替换敏感词
     */
    public String replace(String text, char replaceChar) {
        return sensitiveWordBs.replace(text, replaceChar);
    }

    /**
     * 使用默认替换符 * 进行替换敏感词
     */
    public String replace(String text) {
        return sensitiveWordBs.replace(text);
    }

    /**
     * 返回所有敏感词
     */
    public List<String> findAll(String text) {
        return sensitiveWordBs.findAll(text);
    }
}

