package com.biji.puppeteer.service;

import com.biji.puppeteer.service.dto.LvmamaScenicSpotDTO;
import com.biji.puppeteer.service.dto.query.ScenicSpotQuery;
import com.biji.puppeteer.util.PageResult;
import com.biji.puppeteer.util.Result;

/**
 * create by biji.zhao on 2020/11/13
 */
public interface LvmamaScenicSpotService {

    Result<String> saveOne(LvmamaScenicSpotDTO lvmamaScenicSpotDTO);

    PageResult<LvmamaScenicSpotDTO> queryList(ScenicSpotQuery query);
}
