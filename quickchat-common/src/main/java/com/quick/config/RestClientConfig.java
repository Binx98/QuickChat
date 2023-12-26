package com.quick.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 徐志斌
 * @Date: 2023/12/26 20:39
 * @Version 1.0
 * @Description: RestClientConfig
 */
@Configuration
public class RestClientConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("101.42.13.186", 9200, "http"))
        );
    }
}
