package com.biji.puppeteer.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * create by biji.zhao on 2020/11/28
 */
public class TreeDTO implements Serializable {

    private String name;
    private List<TreeDTO> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeDTO> children) {
        this.children = children;
    }
}
