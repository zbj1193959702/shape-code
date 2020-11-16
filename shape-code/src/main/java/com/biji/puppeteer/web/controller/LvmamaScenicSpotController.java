package com.biji.puppeteer.web.controller;

import com.alibaba.fastjson.JSON;
import com.biji.puppeteer.service.LvmamaScenicSpotService;
import com.biji.puppeteer.service.dto.LvmamaScenicSpotDTO;
import com.biji.puppeteer.service.dto.query.ScenicSpotQuery;
import com.biji.puppeteer.util.PageResult;
import com.biji.puppeteer.util.Result;
import com.biji.puppeteer.web.vo.JsonReturn;
import com.biji.puppeteer.web.vo.LvmamaScenicSpotVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * create by biji.zhao on 2020/11/13
 */
@Controller
@RequestMapping("/scenicSpot")
public class LvmamaScenicSpotController {


    @Autowired
    LvmamaScenicSpotService lvmamaScenicSpotService;

    @RequestMapping("save")
    @ResponseBody
    public JsonReturn saveOne(LvmamaScenicSpotDTO spotDTO) {
        Result<String> result = lvmamaScenicSpotService.saveOne(spotDTO);
        if (result.isSuccess()) {
            return JsonReturn.successInstance();
        }
        return JsonReturn.errorInstance(result.getMsg());
    }

    @RequestMapping("table")
    public String customerPage() {
        return "scenicSpot";
    }


    @RequestMapping(value = "list")
    @ResponseBody
    public JsonReturn queryList(String json) {
        ScenicSpotQuery query = JSON.parseObject(json, ScenicSpotQuery.class);
        PageResult<LvmamaScenicSpotDTO> pageResult = lvmamaScenicSpotService.queryList(query);

        PageResult<LvmamaScenicSpotVO> result = new PageResult<>();
        result.setTotalRecordCount(pageResult.getTotalRecordCount());
        List<LvmamaScenicSpotVO> voList = Lists.newArrayList();
        pageResult.getRecords().forEach(r -> voList.add(LvmamaScenicSpotVO.dto2VO(r)));
        result.setRecords(voList);
        return JsonReturn.successInstance(result);
    }
}