package com.biji.puppeteer.shapecode.service.dto;

import java.util.Map;

/**
 * create by biji.zhao on 2020/11/11
 */
public class ElasticEntity {
    private String id;
    private Map data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
