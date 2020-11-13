package com.biji.puppeteer.service;

import com.biji.puppeteer.service.dto.CustomerDTO;
import com.biji.puppeteer.service.dto.query.CustomerQuery;
import com.biji.puppeteer.util.PageResult;
import com.biji.puppeteer.util.Result;

import java.util.List;

/**
 * create by biji.zhao on 2020/11/12
 */
public interface CustomerService {

    Result saveOne(CustomerDTO customerDTO);

    PageResult<CustomerDTO> queryList(CustomerQuery customerQuery);
}
