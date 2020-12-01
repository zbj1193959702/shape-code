package com.biji.puppeteer.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * create by biji.zhao on 2020/11/24
 */
public class RegionServiceTest extends BaseServiceTest {
    @Autowired
    private RegionService regionService;

    @Test
    public void test_saveProvince() {
        regionService.saveProvince();
    }

    @Test
    public void test_saveCity() {
        regionService.saveCity();
    }

    @Test
    public void test_saveDistrict() {
        regionService.saveDistrict();
    }

    @Test
    public void test_getRegionJson() {
        regionService.getRegionJson();
    }
}
