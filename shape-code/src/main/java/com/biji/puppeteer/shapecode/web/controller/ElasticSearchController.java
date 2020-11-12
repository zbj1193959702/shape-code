package com.biji.puppeteer.shapecode.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.biji.puppeteer.shapecode.service.dto.ResponseResult;
import com.biji.puppeteer.shapecode.service.enums.ResponseCode;
import com.biji.puppeteer.shapecode.service.impl.BaseElasticService;
import com.biji.puppeteer.shapecode.web.vo.IdxVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * create by biji.zhao on 2020/11/12
 */
@RequestMapping("/elasticSearch")
@RestController
public class ElasticSearchController {

    @Autowired
    BaseElasticService elasticService;

    @RequestMapping(value = "/createIndex",method = RequestMethod.POST)
    public ResponseResult createIndex(@RequestBody IdxVo idxVo){
        ResponseResult response = new ResponseResult();
        try {
            //索引不存在，再创建，否则不允许创建
            if(!elasticService.indexExist(idxVo.getIdxName())){
                String idxSql = JSONObject.toJSONString(idxVo.getIdxSql());
                elasticService.createIndex(idxVo.getIdxName(),idxSql);
            } else{
                response.setStatus(false);
                response.setCode(ResponseCode.DUPLICATEKEY_ERROR_CODE.getCode());
                response.setMsg("索引已经存在，不允许创建");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setCode(ResponseCode.ERROR.getCode());
            response.setMsg(ResponseCode.ERROR.getMsg());
        }
        return response;
    }

    /**
     * @Description 判断索引是否存在；存在-TRUE，否则-FALSE
     * @param index
     * @return xyz.wongs.weathertop.base.message.response.ResponseResult
     * @throws
     * @date 2019/11/19 18:48
     */
    @RequestMapping(value = "/exist/{index}")
    public ResponseResult indexExist(@PathVariable(value = "index") String index){

        ResponseResult response = new ResponseResult();
        try {
            if(!elasticService.isExistsIndex(index)){
                response.setCode(ResponseCode.RESOURCE_NOT_EXIST.getCode());
                response.setMsg(ResponseCode.RESOURCE_NOT_EXIST.getMsg());
            } else {
                response.setMsg(" 索引已经存在, " + index);
            }
        } catch (Exception e) {
            response.setCode(ResponseCode.NETWORK_ERROR.getCode());
            response.setMsg(" 调用ElasticSearch 失败！");
            response.setStatus(false);
        }
        return response;
    }

}
