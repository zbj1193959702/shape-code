package com.biji.puppeteer.dao.mapper;

import com.biji.puppeteer.dao.model.EsScenicSpot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * create by biji.zhao on 2020/11/16
 */
@Repository
public interface ScenicSpotEsMapper extends ElasticsearchRepository<EsScenicSpot, Long> {
}
