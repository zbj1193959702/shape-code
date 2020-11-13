package com.biji.puppeteer.service.dto.query;

/**
 * create by biji.zhao on 2020/11/13
 */
public class CustomerQuery {
    private String phone;
    private Integer pageStart;
    private Integer pageSize;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPageStart() {
        return pageStart;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
