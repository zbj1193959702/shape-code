package com.biji.puppeteer.shapecode.service;

import java.util.List;
import java.util.Map;

/**
 * create by biji.zhao on 2020/11/12
 */
public interface ElasticSearchService {
    /**
     * 添加数据
     *
     * @return
     * @throws Exception
     */
    Boolean addData() throws Exception;

    /**
     * 搜索
     *
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> searchData(String keyword, int pageNo, int pageSize) throws Exception;

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
