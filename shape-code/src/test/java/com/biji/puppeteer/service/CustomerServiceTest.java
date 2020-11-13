package com.biji.puppeteer.service;

import com.biji.puppeteer.service.dto.CustomerDTO;
import com.biji.puppeteer.service.dto.query.CustomerQuery;
import com.biji.puppeteer.util.PageResult;
import com.biji.puppeteer.util.Result;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * create by biji.zhao on 2020/11/12
 */
public class CustomerServiceTest extends BaseServiceTest {

    @Autowired
    CustomerService customerService;

    @Test
    public void test_saveOne() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setPhone("19985757132");
        Result result = customerService.saveOne(customerDTO);
        assert result.isSuccess();
    }

    @Test
    public void test_queryList() {
        CustomerQuery customerQuery = new CustomerQuery();
//        customerQuery.setPhone("19985757132");
        customerQuery.setPageSize(20);
        customerQuery.setPageStart(1);
        PageResult pageResult = customerService.queryList(customerQuery);
        assert pageResult.getTotalRecordCount() == 20;
    }
}
