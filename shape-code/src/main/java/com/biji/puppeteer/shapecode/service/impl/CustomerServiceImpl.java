package com.biji.puppeteer.shapecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.biji.puppeteer.shapecode.dao.mapper.CustomerMapper;
import com.biji.puppeteer.shapecode.dao.model.Customer;
import com.biji.puppeteer.shapecode.service.CustomerService;
import com.biji.puppeteer.shapecode.service.dto.CustomerDTO;
import com.biji.puppeteer.shapecode.service.enums.CommonStatus;
import com.biji.puppeteer.shapecode.util.CheckParamsUtil;
import com.biji.puppeteer.shapecode.util.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * create by biji.zhao on 2020/11/12
 */
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public Result saveOne(CustomerDTO customerDTO) {
        String check = CheckParamsUtil.check(customerDTO, CustomerDTO.class, "phone");
        if (check != null) {
            return Result.errorInstance(check);
        }
        LambdaQueryWrapper<Customer> query = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getStatus, CommonStatus.no_delete.value)
                .eq(Customer::getPhone, customerDTO.getPhone());

        List<Customer> customers = customerMapper.selectList(query);
        if (CollectionUtils.isNotEmpty(customers)) {
            return Result.errorInstance("客户已存在");
        }
        Customer customer = new Customer();
        customer.setPhone(customerDTO.getPhone());
        customer.setStatus(CommonStatus.no_delete.value);
        customerMapper.insert(customer);
        return Result.successInstance();
    }
}
