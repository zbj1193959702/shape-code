package com.biji.puppeteer.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * create by biji.zhao on 2020/11/24
 */
public class RegionResponse implements Serializable {
    private String status;
    private String info;
    private String infoCode;
    private String count;
    private List<District>  districts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(String infoCode) {
        this.infoCode = infoCode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
}
