package com.biji.puppeteer.shapecode.web.controller;

import com.biji.puppeteer.shapecode.service.CustomerService;
import com.biji.puppeteer.shapecode.service.dto.CustomerDTO;
import com.biji.puppeteer.shapecode.util.Result;
import com.biji.puppeteer.shapecode.web.vo.JsonReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by biji.zhao on 2020/11/12
 */
@RequestMapping("/customer")
@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping("save")
    public JsonReturn saveOne(String phone) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setPhone(phone);
        Result result = customerService.saveOne(customerDTO);
        if (result.isSuccess()) {
            return JsonReturn.successInstance();
        }
        return JsonReturn.errorInstance(result.getMsg());
    }
}
