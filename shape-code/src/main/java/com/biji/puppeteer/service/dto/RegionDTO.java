package com.biji.puppeteer.service.dto;

import java.io.Serializable;

/**
 * create by biji.zhao on 2020/11/27
 */
public class RegionDTO implements Serializable {
    private Integer id;
    private String name;
    private String regionCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }
}
