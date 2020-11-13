package com.biji.puppeteer.web.controller;

import com.alibaba.fastjson.JSON;
import com.biji.puppeteer.service.dto.CustomerDTO;
import com.biji.puppeteer.service.CustomerService;
import com.biji.puppeteer.service.dto.query.CustomerQuery;
import com.biji.puppeteer.util.PageResult;
import com.biji.puppeteer.util.Result;
import com.biji.puppeteer.web.vo.CustomerVO;
import com.biji.puppeteer.web.vo.JsonReturn;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * create by biji.zhao on 2020/11/12
 */
@RequestMapping("/customer")
@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping("save")
    @ResponseBody
    public JsonReturn saveOne(String phone) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setPhone(phone);
        Result result = customerService.saveOne(customerDTO);
        if (result.isSuccess()) {
            return JsonReturn.successInstance();
        }
        return JsonReturn.errorInstance(result.getMsg());
    }

    @RequestMapping("table")
    public String customerPage() {
        return "brandForm";
    }

    @RequestMapping(value = "list")
    @ResponseBody
    public JsonReturn queryList(String json) {
        CustomerQuery query = JSON.parseObject(json, CustomerQuery.class);
        PageResult<CustomerDTO> pageResult = customerService.queryList(query);

        PageResult<CustomerVO> result = new PageResult<>();
        result.setTotalRecordCount(pageResult.getTotalRecordCount());
        List<CustomerVO> voList = Lists.newArrayList();
        pageResult.getRecords().forEach(r -> voList.add(CustomerVO.dto2VO(r)));
        result.setRecords(voList);
        return JsonReturn.successInstance(result);
    }
}
