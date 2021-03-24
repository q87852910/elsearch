package com.example.elsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ElasticSearchConfig
 * @Description: TODO
 * @author: 大佬
 * @Date: 2021/3/24 19:37
 * @Version: 1.0
 **/
@Configuration
public class ElasticSearchConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                new HttpHost("123.57.68.235", 9200, "http")));
        return restHighLevelClient;
    }
}
