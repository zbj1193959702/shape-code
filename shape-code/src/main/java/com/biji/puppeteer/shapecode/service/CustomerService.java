package com.biji.puppeteer.shapecode.service;

import com.biji.puppeteer.shapecode.service.dto.CustomerDTO;
import com.biji.puppeteer.shapecode.util.Result;

/**
 * create by biji.zhao on 2020/11/12
 */
public interface CustomerService {

    Result saveOne(CustomerDTO customerDTO);
}