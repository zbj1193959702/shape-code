package com.biji.puppeteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biji.puppeteer.dao.mapper.CustomerMapper;
import com.biji.puppeteer.service.dto.query.CustomerQuery;
import com.biji.puppeteer.service.enums.CommonStatus;
import com.biji.puppeteer.dao.model.Customer;
import com.biji.puppeteer.service.CustomerService;
import com.biji.puppeteer.service.dto.CustomerDTO;
import com.biji.puppeteer.util.CheckParamsUtil;
import com.biji.puppeteer.util.PageResult;
import com.biji.puppeteer.util.Result;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by biji.zhao on 2020/11/12
 */
@Service
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

    @Override
    public PageResult<CustomerDTO> queryList(CustomerQuery customerQuery) {
        IPage<Customer> customerIPage = new Page<>(customerQuery.getPageStart(), customerQuery.getPageSize());
        LambdaQueryWrapper<Customer> query = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getStatus, CommonStatus.no_delete.value)
                .le(StringUtils.isNotEmpty(customerQuery.getPhone()), Customer::getPhone, customerQuery.getPhone());

        customerIPage = customerMapper.selectPage(customerIPage, query);

        PageResult<CustomerDTO> pageResult = new PageResult<>();
        pageResult.setTotalRecordCount(Math.toIntExact(customerIPage.getTotal()));
        List<CustomerDTO> dtoList = Lists.newArrayList();
        customerIPage.getRecords().forEach(r -> dtoList.add(model2DTO(r)));
        pageResult.setRecords(dtoList);
        return pageResult;
    }

    private static CustomerDTO model2DTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setStatus(customer.getStatus());
        customerDTO.setCreateTime(customer.getCreateTime());
        customerDTO.setUpdateTime(customer.getUpdateTime());
        return customerDTO;
    }
}
