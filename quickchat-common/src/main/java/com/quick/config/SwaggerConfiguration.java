package com.quick.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * @Author 徐志斌
 * @Date: 2024/04/28 09:21
 * @Description: 处理：java.lang.NullPointerException: Cannot invoke “org.springframework.web
 * .servlet.mvc.condition.PatternsRequestCondition.getPatterns()” because “this.condition” is null
 * @Version 1.0
 * -----------------------------------------------------------------
 * 文档访问地址：http://ip:port/swagger-ui/index.html
 * 添加Knife4j可以导出导出离线文档，访问地址：http://ip:port/doc.html
 */
@Configuration
@EnableKnife4j
@EnableOpenApi
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "dev")
public class SwaggerConfiguration {
    @Bean
    public Docket createRestApis() {
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .groupName("spring-boot-2.7.9")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .ignoredParameterTypes(CookieValue.class)
                .apiInfo(apiInfo())
                .select()
                //以下拦截配置可以三选一，根据需要进行添加
                .apis(RequestHandlerSelectors.basePackage("com.quick.controller"))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("使用swagger生成的接口文档")
                .description("开发测试")
                .termsOfServiceUrl("https://www.baidu.com/")
                .version("0.0.1")
                .build();
    }
}