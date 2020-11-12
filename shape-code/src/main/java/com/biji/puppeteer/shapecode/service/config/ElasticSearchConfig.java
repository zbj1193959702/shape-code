package com.biji.puppeteer.shapecode.service.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create by biji.zhao on 2020/11/12
 */
@Configuration
public class ElasticSearchConfig {
    @Value("${elasticsearch.hostname}")
    private String hostname;

    @Value("${elasticsearch.port}")
    private int port;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        return new RestHighLevelClient(RestClient.builder(new HttpHost(hostname, port, "http")));
    }
}
