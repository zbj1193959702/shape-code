package com.biji.puppeteer.shapecode.dao.mapper;

import com.biji.puppeteer.shapecode.dao.model.ElasticSearchParams;

import java.util.List;

/**
 * create by biji.zhao on 2020/11/12
 */
public interface ElasticSearchMapper {

    List<ElasticSearchParams> getAllDataList();
}
