package com.mengxuegu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * create by biji.zhao on 2020/10/18
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer_6002 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServer_6002.class, args);
    }
}
