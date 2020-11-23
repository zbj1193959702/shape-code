package com.biji.puppeteer;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@MapperScan(basePackages = {"com.biji.puppeteer.dao.mapper"})
@EnableApolloConfig
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.biji.puppeteer.dao.mapper")
public class ShapeCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShapeCodeApplication.class, args);
    }

}
