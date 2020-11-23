package com.biji.puppeteer.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biji.puppeteer.dao.model.LvmamaScenicSpot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * create by biji.zhao on 2020/11/13
 */
@Mapper
public interface LvmamaScenicSpotMapper extends BaseMapper<LvmamaScenicSpot> {

    List<LvmamaScenicSpot> findAll();
}
