package com.mengxuegu.springcloud.service;

import com.mengxuegu.springcloud.entities.Product;

import java.util.List;

/**
 * create by biji.zhao on 2020/10/18
 */
public interface ProductService {
    boolean add(Product product);

    Product get(Long id);

    List<Product> list();
}
