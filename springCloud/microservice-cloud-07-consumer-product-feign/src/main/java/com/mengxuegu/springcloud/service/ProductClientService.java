package com.mengxuegu.springcloud.service;

import com.mengxuegu.springcloud.entities.Product;
import com.mengxuegu.springcloud.service.impl.ProductClientServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * create by biji.zhao on 2020/10/18
 */

// fallback = ProductClientServiceFallBack.class   说明：feign 自带熔断机制
@FeignClient(value = "MICROSERVICE-PRODUCT", fallback = ProductClientServiceFallBack.class)
public interface ProductClientService {
    /**
     * 特别注意 使用 feign时  参数的注解需要对应加上
     * @param id
     * @return
     */
    @RequestMapping(value = "/product/get/{id}",method = RequestMethod.GET)
    Product get(@PathVariable("id") Long id);

    @RequestMapping(value = "/product/list",method = RequestMethod.GET)
    List<Product> list();

    @RequestMapping(value = "/product/add",method = RequestMethod.POST)
    boolean add(Product product);
}
