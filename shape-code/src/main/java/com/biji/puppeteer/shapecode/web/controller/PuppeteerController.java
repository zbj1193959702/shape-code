package com.biji.puppeteer.shapecode.web.controller;

import com.alibaba.druid.util.StringUtils;
import com.biji.puppeteer.shapecode.service.PuppeteerService;
import com.biji.puppeteer.shapecode.web.vo.JsonReturn;
import com.biji.puppeteer.shapecode.web.vo.SummaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

/**
 * create by biji.zhao on 2020/11/11
 */
@RequestMapping("/puppeteer")
@RestController
public class PuppeteerController {

    @Autowired
    PuppeteerService puppeteerService;

    @RequestMapping("/save")
    public JsonReturn login() {
        return JsonReturn.successInstance();
    }

    public static void main(String[] args) {
        List<SummaryVO> summaryVOS = getSummaryVOS();
        Integer sumCount = summaryVOS.stream()
                .map(SummaryVO::getTotalCount).reduce(0, Integer::sum);
        summaryVOS.stream()
                .map(SummaryVO::getTotalCount).reduce(Integer::sum).ifPresent(System.out::println);
        String join = summaryVOS.stream()
                .map(e -> String.valueOf(e.getTotalCount())).collect(Collectors.joining("|"));
        Integer reduceSum = summaryVOS.stream()
                .filter(e -> e.getUserVO() != null)
                .map(e -> e.getUserVO().getId()).reduce(0, Integer::sum);
        boolean b = summaryVOS.stream()
                .filter(e -> !CollectionUtils.isEmpty(e.getUserVOList()))
                .flatMap(e -> e.getUserVOList().stream())
                .filter(e -> !StringUtils.isEmpty(e.getPassword()))
                .map(e -> e.getPassword().toLowerCase())
                .distinct().anyMatch("xxx"::contains);
        List<SummaryVO> treeSummaryList = summaryVOS.stream()
                .collect(collectingAndThen(
                        toCollection(
                                () -> new TreeSet<>(comparing(SummaryVO::getTotalCount))), ArrayList::new)
                );
        LinkedHashMap<Integer, List<SummaryVO>> LinkHashMap = summaryVOS.stream()
                .collect(groupingBy(SummaryVO::getTotalCount, LinkedHashMap::new, toList()));
        summaryVOS.stream()
                .map(SummaryVO::getSummaryMap)
                .filter(Objects::nonNull)
                .forEach(map -> map.forEach((k, v) -> System.out.println(k + v)));
        Set<SummaryVO> set = new TreeSet<>((one, two) -> Objects.equals(one.getTotalCount(), two.getTotalCount()) ? 0 : 1);
        set.addAll(summaryVOS);
        set.stream().map(SummaryVO::getTotalCount)
                .skip(1).limit(5).reduce((x, y) -> x - y).ifPresent(System.out::println);
    }

    private static List<SummaryVO> getSummaryVOS() {
        List<SummaryVO> summaryVOS = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SummaryVO summaryVO = new SummaryVO();
            summaryVO.setTotalCount(i);
            summaryVOS.add(summaryVO);
        }
        return summaryVOS;
    }
}
