package com.biji.puppeteer.service;

import com.biji.puppeteer.dao.model.EsScenicSpot;
import com.biji.puppeteer.service.dto.LvmamaScenicSpotDTO;
import com.biji.puppeteer.util.PageResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * create by biji.zhao on 2020/11/12
 */
public interface ElasticSearchService {

    void importLvmama() throws Exception;

    void deleteAll();

    PageResult<LvmamaScenicSpotDTO> queryScenicSpot(String text, Integer pageStart, Integer pageSize) throws IOException;

    /**
     * 删除索引
     *
     * @return
     * @throws Exception
     */
    Boolean deleteIndex() throws Exception;

    /**
     * 查询索引是否存在
     *
     * @return
     * @throws Exception
     */
    Boolean searchIndex() throws Exception;

    /**
     * 创建索引
     *
     * @return
     * @throws Exception
     */
    String createIndex() throws Exception;
}
