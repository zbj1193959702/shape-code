package com.biji.puppeteer;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.biji.puppeteer.dao.mapper"})
@EnableApolloConfig
@SpringBootApplication
public class ShapeCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShapeCodeApplication.class, args);
    }

}
