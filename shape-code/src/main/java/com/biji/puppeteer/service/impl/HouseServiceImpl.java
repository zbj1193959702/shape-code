package com.biji.puppeteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biji.puppeteer.dao.mapper.HouseMapper;
import com.biji.puppeteer.dao.model.House;
import com.biji.puppeteer.dao.model.LvmamaScenicSpot;
import com.biji.puppeteer.service.HouseService;
import com.biji.puppeteer.service.dto.HouseSaveDTO;
import com.biji.puppeteer.service.dto.LvmamaScenicSpotDTO;
import com.biji.puppeteer.service.dto.query.ScenicSpotQuery;
import com.biji.puppeteer.service.enums.CommonStatus;
import com.biji.puppeteer.service.enums.HouseTypeEnum;
import com.biji.puppeteer.util.PageResult;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * create by biji.zhao on 2020/11/28
 */
@Service
public class HouseServiceImpl implements HouseService {
    @Autowired
    private HouseMapper houseMapper;

    @Override
    public void saveOne(HouseSaveDTO saveDTO) {
        if (saveDTO == null) {
            return;
        }
        try {
            String norms = saveDTO.getNorms();
            String area = norms.substring(norms.indexOf("|") + 1, norms.indexOf("平米")).trim();
            House house = new House();
            house.setTitle(saveDTO.getTitle());
            house.setAddress(saveDTO.getAddress());
            house.setPrice(
                    Integer.parseInt(saveDTO.getPrice()
                            .replace("单价", "")
                            .replace("元/平米", ""))
            );
            house.setArea(Double.valueOf(area));
            house.setDetailUrl(saveDTO.getDetailUrl());
            house.setFirstImage(saveDTO.getFirstImage());
            house.setHouseType(HouseTypeEnum.er_shou_fang.value);
            house.setCreateTime(new Date());
            house.setStatus(CommonStatus.no_delete.value);
            house.setNorms(norms);
            houseMapper.insert(house);
        }catch (Exception e) {

        }
    }

    @Override
    public PageResult<House> queryList(ScenicSpotQuery query) {
        IPage<House> scenicSpotIPage = new Page<>(query.getPageStart(), query.getPageSize());
        LambdaQueryWrapper<House> queryWrapper = new LambdaQueryWrapper<House>()
                .eq(House::getStatus, CommonStatus.no_delete.value)
                .like(StringUtils.isNotEmpty(query.getTitle()) , House::getTitle, query.getTitle());
        scenicSpotIPage = houseMapper.selectPage(scenicSpotIPage, queryWrapper);
        PageResult<House> result = new PageResult<>();
        result.setTotalRecordCount(Math.toIntExact(scenicSpotIPage.getTotal()));
        result.setRecords(scenicSpotIPage.getRecords());
        return result;
    }
}
