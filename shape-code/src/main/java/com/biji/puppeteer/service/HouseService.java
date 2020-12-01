package com.biji.puppeteer.service;

import com.biji.puppeteer.dao.model.House;
import com.biji.puppeteer.service.dto.HouseSaveDTO;
import com.biji.puppeteer.service.dto.query.ScenicSpotQuery;
import com.biji.puppeteer.util.PageResult;

/**
 * create by biji.zhao on 2020/11/28
 */
public interface HouseService {

    void saveOne(HouseSaveDTO saveDTO);

    PageResult<House> queryList(ScenicSpotQuery query);
}
