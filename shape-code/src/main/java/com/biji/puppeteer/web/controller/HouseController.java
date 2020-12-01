package com.biji.puppeteer.web.controller;

import com.alibaba.fastjson.JSON;
import com.biji.puppeteer.dao.model.House;
import com.biji.puppeteer.service.HouseService;
import com.biji.puppeteer.service.dto.HouseSaveDTO;
import com.biji.puppeteer.service.dto.query.ScenicSpotQuery;
import com.biji.puppeteer.util.PageResult;
import com.biji.puppeteer.web.vo.JsonReturn;
import com.biji.puppeteer.web.vo.LvmamaScenicSpotVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * create by biji.zhao on 2020/11/28
 */
@RequestMapping("/house")
@Controller
public class HouseController {

    @Autowired
    private HouseService houseService;

    @RequestMapping("/saveOne")
    @ResponseBody
    public JsonReturn saveOne(HouseSaveDTO saveDTO) {
        if (saveDTO == null) {
            return JsonReturn.errorInstance("empty");
        }
        houseService.saveOne(saveDTO);
        return JsonReturn.successInstance();
    }

    @RequestMapping(value = "list")
    @ResponseBody
    public JsonReturn queryList(String json)  {
        ScenicSpotQuery query = JSON.parseObject(json, ScenicSpotQuery.class);
        PageResult<House> result = houseService.queryList(query);
        return JsonReturn.successInstance(result);
    }

    @RequestMapping("table")
    public String customerPage() {
        return "house";
    }

    @RequestMapping("map3d")
    public String map3d() {
        return "map3d";
    }
}
