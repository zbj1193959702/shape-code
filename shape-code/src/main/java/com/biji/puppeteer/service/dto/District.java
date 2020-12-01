package com.biji.puppeteer.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * create by biji.zhao on 2020/11/24
 */
public class District implements Serializable {

    private String adcode;
    private String name;
    private String center;
    private String level;
    private List<District> districts;

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
}
