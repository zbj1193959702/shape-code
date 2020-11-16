package com.biji.puppeteer.web.vo;

import com.biji.puppeteer.service.dto.LvmamaScenicSpotDTO;
import com.biji.puppeteer.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * create by biji.zhao on 2020/11/13
 */
public class LvmamaScenicSpotVO implements Serializable {
    private Integer id;
    private String title;
    private Integer price;
    private String photoUrl;
    private String common;
    private String createTime;
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
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

    public static LvmamaScenicSpotVO dto2VO(LvmamaScenicSpotDTO dto) {
        if (dto == null) {
            return null;
        }
        LvmamaScenicSpotVO lvmamaScenicSpotVO = new LvmamaScenicSpotVO();
        lvmamaScenicSpotVO.setId(dto.getId());
        lvmamaScenicSpotVO.setTitle(dto.getTitle());
        lvmamaScenicSpotVO.setPrice(dto.getPrice());
        lvmamaScenicSpotVO.setPhotoUrl(dto.getPhotoUrl());
        lvmamaScenicSpotVO.setCommon(dto.getCommon());
        lvmamaScenicSpotVO.setCreateTime(DateUtil.formatDate(dto.getCreateTime()));
        lvmamaScenicSpotVO.setUpdateTime(DateUtil.formatDate(dto.getUpdateTime()));
        return lvmamaScenicSpotVO;
    }
}
