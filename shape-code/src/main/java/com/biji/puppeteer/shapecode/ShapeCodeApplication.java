package com.biji.puppeteer.shapecode;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {" com.biji.puppeteer.shapecode.dao.mapper"})
@EnableApolloConfig
public class ShapeCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShapeCodeApplication.class, args);
    }

}
