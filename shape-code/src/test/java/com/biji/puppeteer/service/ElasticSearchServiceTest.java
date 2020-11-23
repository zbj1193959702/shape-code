package com.biji.puppeteer.service;

import com.biji.puppeteer.dao.model.EsScenicSpot;
import com.biji.puppeteer.service.dto.LvmamaScenicSpotDTO;
import com.biji.puppeteer.util.PageResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * create by biji.zhao on 2020/11/16
 */
public class ElasticSearchServiceTest extends BaseServiceTest {
    @Autowired
    ElasticSearchService elasticSearchService;

    @Test
    public void importLvmama(){
        try {
            elasticSearchService.importLvmama();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_deleteAll(){
        elasticSearchService.deleteAll();
    }

    @Test
    public void es_query(){
        try {
            PageResult<LvmamaScenicSpotDTO> esList = elasticSearchService.queryScenicSpot(
                    "贵州黄果树、荔波大小七孔", 1, 10);
            System.out.println(esList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
