package com.biji.puppeteer.web.vo;

import com.biji.puppeteer.service.dto.CustomerDTO;
import com.biji.puppeteer.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * create by biji.zhao on 2020/11/12
 */
public class CustomerVO implements Serializable {

    private Integer id;
    private String phone;
    private Integer status;
    private String createTime;
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public static CustomerVO dto2VO(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;
        }
        CustomerVO customerVO = new CustomerVO();
        customerVO.setId(customerDTO.getId());
        customerVO.setPhone(customerDTO.getPhone());
        customerVO.setStatus(customerDTO.getStatus());
        customerVO.setCreateTime(DateUtil.formatDate(customerDTO.getCreateTime()));
        customerVO.setUpdateTime(DateUtil.formatDate(customerDTO.getUpdateTime()));
        return customerVO;
    }
}
