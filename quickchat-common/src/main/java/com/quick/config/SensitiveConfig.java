package com.quick.config;

import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.quick.sensitive.MyWordAllow;
import com.quick.sensitive.MyWordDeny;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-03-19  11:01
 * @Description: Sensitive 敏感词配置
 * @Version: 1.0
 */
@Configuration
public class SensitiveConfig {
    IWordDeny wordDeny = WordDenys.chains(WordDenys.system(), new MyWordDeny());
    IWordAllow wordAllow = WordAllows.chains(WordAllows.system(), new MyWordAllow());

    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                // 忽略大小写
                .ignoreCase(true)
                // 忽略半角圆角
                .ignoreWidth(true)
                // 忽略数字的写法
                .ignoreNumStyle(true)
                // 忽略中文的书写格式：简繁体
                .ignoreChineseStyle(true)
                // 忽略英文的书写格式
                .ignoreEnglishStyle(true)
                // 忽略重复词
                .ignoreRepeat(false)
                // 是否启用数字检测
                .enableNumCheck(true)
                // 是否启用邮箱检测
                .enableEmailCheck(true)
                // 是否启用链接检测
                .enableUrlCheck(true)
                // 配置自定义敏感词
                .wordDeny(wordDeny)
                // 配置自定义非敏感词
                .wordAllow(wordAllow)
                // 数字检测，自定义指定长度
                .numCheckLen(8)
                .init();
    }

    public static void main(String[] args) {
        String str = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前。";
        System.out.println(SensitiveWordHelper.contains(str));
        System.out.println(SensitiveWordHelper.findAll(str));
    }
}
