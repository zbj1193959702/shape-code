package com.biji.puppeteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biji.puppeteer.dao.mapper.LvmamaScenicSpotMapper;
import com.biji.puppeteer.dao.model.LvmamaScenicSpot;
import com.biji.puppeteer.service.LvmamaScenicSpotService;
import com.biji.puppeteer.service.dto.LvmamaScenicSpotDTO;
import com.biji.puppeteer.service.dto.query.ScenicSpotQuery;
import com.biji.puppeteer.util.CheckParamsUtil;
import com.biji.puppeteer.util.PageResult;
import com.biji.puppeteer.util.Result;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * create by biji.zhao on 2020/11/13
 */
@Service
public class LvmamaScenicSpotServiceImpl implements LvmamaScenicSpotService {

    @Autowired
    LvmamaScenicSpotMapper lvmamaScenicSpotMapper;

    @Override
    public Result<String> saveOne(LvmamaScenicSpotDTO scenicSpotDTO) {
        String check = CheckParamsUtil.check(scenicSpotDTO,
                LvmamaScenicSpotDTO.class, "title", "price", "photoUrl");
        if (check != null) {
            return Result.errorInstance(check);
        }
        LambdaQueryWrapper<LvmamaScenicSpot> query = new LambdaQueryWrapper<LvmamaScenicSpot>()
                .eq(LvmamaScenicSpot::getTitle, scenicSpotDTO.getTitle());
        List<LvmamaScenicSpot> scenicSpots = lvmamaScenicSpotMapper.selectList(query);
        if (CollectionUtils.isNotEmpty(scenicSpots)) {
            return Result.errorInstance("景点已经存在");
        }
        lvmamaScenicSpotMapper.insert(dto2Model(scenicSpotDTO));
        return Result.successInstance();
    }

    @Override
    public PageResult<LvmamaScenicSpotDTO> queryList(ScenicSpotQuery query) {
        IPage<LvmamaScenicSpot> scenicSpotIPage = new Page<>(query.getPageStart(), query.getPageSize());
        LambdaQueryWrapper<LvmamaScenicSpot> queryWrapper = new LambdaQueryWrapper<LvmamaScenicSpot>()
                .like(StringUtils.isNotEmpty(query.getCommon()) , LvmamaScenicSpot::getCommon, query.getCommon())
                .like(StringUtils.isNotEmpty(query.getTitle()) , LvmamaScenicSpot::getTitle, query.getTitle())
                .gt(query.getMinPrice() != null, LvmamaScenicSpot::getPrice, query.getMinPrice())
                .lt(query.getMaxPrice() != null, LvmamaScenicSpot::getPrice, query.getMaxPrice());
        scenicSpotIPage = lvmamaScenicSpotMapper.selectPage(scenicSpotIPage, queryWrapper);

        PageResult<LvmamaScenicSpotDTO> result = new PageResult<>();
        result.setTotalRecordCount(Math.toIntExact(scenicSpotIPage.getTotal()));
        List<LvmamaScenicSpotDTO> scenicSpotList = Lists.newArrayList();
        scenicSpotIPage.getRecords().forEach(s -> scenicSpotList.add(model2dto(s)));
        result.setRecords(scenicSpotList);
        return result;
    }

    private static LvmamaScenicSpotDTO model2dto(LvmamaScenicSpot model) {
        if (model == null) {
            return null;
        }
        LvmamaScenicSpotDTO dto = new LvmamaScenicSpotDTO();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setPrice(model.getPrice());
        dto.setPhotoUrl(model.getPhotoUrl());
        dto.setCommon(model.getCommon());
        dto.setCreateTime(model.getCreateTime());
        dto.setUpdateTime(model.getUpdateTime());
        return dto;
    }

    private static LvmamaScenicSpot dto2Model(LvmamaScenicSpotDTO dto) {
        if (dto == null) {
            return null;
        }
        LvmamaScenicSpot model = new LvmamaScenicSpot();
        model.setId(dto.getId());
        model.setTitle(dto.getTitle());
        model.setPrice(dto.getPrice());
        model.setPhotoUrl(dto.getPhotoUrl());
        model.setCommon(dto.getCommon());
        model.setCreateTime(dto.getCreateTime() == null ? new Date() : dto.getCreateTime());
        model.setUpdateTime(dto.getUpdateTime() == null ? new Date() : dto.getUpdateTime());
        return model;
    }
}
