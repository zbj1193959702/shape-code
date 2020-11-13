package com.mengxuegu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * create by biji.zhao on 2020/10/18
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages= {"com.mengxuegu.springcloud"}) // 会扫描标记了指定包下@FeignClient注解的接口，并生成此接口的代理对象
public class ProductConsumer_80_Feign {
    public static void main(String[] args) {
        SpringApplication.run(ProductConsumer_80_Feign.class, args);
    }
}
