package com.biji.puppeteer.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.biji.puppeteer.dao.mapper.RegionMapper;
import com.biji.puppeteer.dao.model.Region;
import com.biji.puppeteer.service.RegionService;
import com.biji.puppeteer.service.dto.District;
import com.biji.puppeteer.service.dto.RegionDTO;
import com.biji.puppeteer.service.dto.RegionResponse;
import com.biji.puppeteer.service.dto.TreeDTO;
import com.biji.puppeteer.service.enums.CommonStatus;
import com.biji.puppeteer.service.enums.RegionType;
import com.biji.puppeteer.util.HttpUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * create by biji.zhao on 2020/11/24
 */
@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    RegionMapper regionMapper;

    private static final String key = "38a94c36c47c4d50b18b1ceb1ea1d763";

    @Override
    public void saveProvince() {
        try {
            String s = HttpUtil.getHttps(String.format("https://restapi.amap.com/v3/config/district?subdistrict=1&key=%s", key));
            RegionResponse regionResponse = JSON.parseObject(s, RegionResponse.class);
            if (regionResponse == null) {
                return;
            }
            List<District> districts = regionResponse.getDistricts();
            District district = districts.get(0);
            saveRegion(districts.get(0), RegionType.country, null);

            district.getDistricts().forEach(d -> saveRegion(d, RegionType.province, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveRegion(District d, RegionType regionType, Integer parentId) {
        Region province = new Region();
        province.setName(d.getName());
        province.setRegionCode(d.getAdcode());
        province.setType(regionType.value);
        province.setParentId(parentId);
        province.setLongitude(Double.valueOf(d.getCenter().split(",")[0]));
        province.setLatitude(Double.valueOf(d.getCenter().split(",")[1]));

        province.setStatus(CommonStatus.no_delete.value);
        province.setCreateTime(new Date());
        province.setUpdateTime(new Date());
        regionMapper.insert(province);
    }

    @Override
    public void saveCity() {
        LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<Region>()
                .eq(Region::getStatus, CommonStatus.no_delete.value)
                .eq(Region::getType, RegionType.province.value);
        List<Region> regionList = regionMapper.selectList(query);
        regionList.forEach(r -> {
            try {
                String s = HttpUtil.getHttps(String.format("https://restapi.amap.com/v3/config/district?subdistrict=1&key=%s&keywords=%s", key, r.getName()));
                RegionResponse regionResponse = JSON.parseObject(s, RegionResponse.class);
                if (regionResponse == null) {
                    return;
                }
                regionResponse.getDistricts().get(0).getDistricts().forEach(d -> {
                    saveRegion(d, RegionType.city, r.getId());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveDistrict() {
        LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<Region>()
                .eq(Region::getStatus, CommonStatus.no_delete.value)
                .eq(Region::getType, RegionType.city.value);
        List<Region> regionList = regionMapper.selectList(query);
        regionList.forEach(r -> {
            try {
                String s = HttpUtil.getHttps(String.format("https://restapi.amap.com/v3/config/district?subdistrict=1&key=%s&keywords=%s", key, r.getName()));
                RegionResponse regionResponse = JSON.parseObject(s, RegionResponse.class);
                if (regionResponse == null || CollectionUtils.isEmpty(regionResponse.getDistricts())) {
                    return;
                }
                regionResponse.getDistricts().get(0).getDistricts().forEach(d -> {
                    saveRegion(d, RegionType.district, r.getId());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public List<RegionDTO> getProvince() {
        LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<Region>()
                .eq(Region::getStatus, CommonStatus.no_delete.value)
                .eq(Region::getType, RegionType.province.value);
        List<Region> regionList = regionMapper.selectList(query);
        return regionList.stream().map(RegionServiceImpl::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RegionDTO> getChildrenRegion(String code) {
        LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<Region>()
                .eq(Region::getStatus, CommonStatus.no_delete.value)
                .eq(Region::getRegionCode, code);
        Region region = regionMapper.selectOne(query);

        if (region == null) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<Region>()
                .eq(Region::getStatus, CommonStatus.no_delete.value)
                .eq(Region::getParentId, region.getId());
        List<Region> regionList = regionMapper.selectList(wrapper);
        return regionList.stream().map(RegionServiceImpl::toDTO).collect(Collectors.toList());
    }

    public TreeDTO getRegionJson() {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<Region>()
                .eq(Region::getStatus, CommonStatus.no_delete.value)
                .eq(Region::getType, RegionType.country.value);

        Region region = regionMapper.selectOne(wrapper);
        TreeDTO tree = new TreeDTO();
        tree.setName(region.getName());
        LambdaQueryWrapper<Region> provinceQuery = new LambdaQueryWrapper<Region>()
                .eq(Region::getStatus, CommonStatus.no_delete.value)
                .eq(Region::getType, RegionType.province.value);
        List<Region> provinceList = regionMapper.selectList(provinceQuery);
        List<TreeDTO> provinceTreeList = Lists.newArrayList();
        provinceList.forEach(p -> {
            TreeDTO provinceTree = new TreeDTO();
            provinceTree.setName(p.getName());
            LambdaQueryWrapper<Region> cityQuery = new LambdaQueryWrapper<Region>()
                    .eq(Region::getStatus, CommonStatus.no_delete.value)
                    .eq(Region::getParentId, p.getId())
                    .eq(Region::getType, RegionType.city.value);
            List<TreeDTO> cityTreeList = Lists.newArrayList();
            List<Region> cityList = regionMapper.selectList(cityQuery);
            cityList.forEach(c -> {
                TreeDTO cityTree = new TreeDTO();
                cityTree.setName(c.getName());
                LambdaQueryWrapper<Region> districtQuery = new LambdaQueryWrapper<Region>()
                        .eq(Region::getStatus, CommonStatus.no_delete.value)
                        .eq(Region::getParentId, c.getId())
                        .eq(Region::getType, RegionType.district.value);
                List<TreeDTO> districtTreeList = Lists.newArrayList();
                List<Region> districtList = regionMapper.selectList(districtQuery);
                districtList.forEach(d -> {
                    TreeDTO districtTree = new TreeDTO();
                    districtTree.setName(d.getName());
                    districtTreeList.add(districtTree);
                });
                cityTree.setChildren(districtTreeList);
                cityTreeList.add(cityTree);
            });
            provinceTree.setChildren(cityTreeList);
            provinceTreeList.add(provinceTree);
        });
        tree.setChildren(provinceTreeList);
        return tree;
    }

    private static RegionDTO toDTO(Region region) {
        if (region == null) {
            return null;
        }
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(region.getId());
        regionDTO.setName(region.getName());
        regionDTO.setRegionCode(region.getRegionCode());
        return regionDTO;
    }
}
