package com.biji.puppeteer.dao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;

/**
 * create by biji.zhao on 2020/11/16
 */
@Document(indexName = "no_ik_index")
public class EsScenicSpot implements Serializable {

    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Auto)//自动检测类型
    private Integer price;

    @Field(type = FieldType.Auto)//自动检测类型
    private String photoUrl;

    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_max_word")
    private String common;

    @Field(type = FieldType.Auto)//自动检测类型
    private String createTime;

    @Field(type = FieldType.Auto)//自动检测类型
    private String updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
