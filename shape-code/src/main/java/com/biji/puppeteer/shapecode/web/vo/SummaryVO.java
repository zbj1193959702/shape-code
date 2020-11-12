package com.biji.puppeteer.shapecode.web.vo;

import java.util.List;
import java.util.Map;

/**
 * create by biji.zhao on 2020/11/11
 */
public class SummaryVO {
    private List<UserVO> userVOList;
    private UserVO userVO;
    private Map<Integer, String> summaryMap;
    private Integer totalCount;

    public List<UserVO> getUserVOList() {
        return userVOList;
    }

    public void setUserVOList(List<UserVO> userVOList) {
        this.userVOList = userVOList;
    }

    public Map<Integer, String> getSummaryMap() {
        return summaryMap;
    }

    public void setSummaryMap(Map<Integer, String> summaryMap) {
        this.summaryMap = summaryMap;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }
}
