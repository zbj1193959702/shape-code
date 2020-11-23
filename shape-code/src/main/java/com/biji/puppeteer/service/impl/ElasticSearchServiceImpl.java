package com.biji.puppeteer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.biji.puppeteer.dao.mapper.ElasticSearchMapper;
import com.biji.puppeteer.dao.mapper.LvmamaScenicSpotMapper;
import com.biji.puppeteer.dao.mapper.ScenicSpotEsMapper;
import com.biji.puppeteer.dao.model.EsScenicSpot;
import com.biji.puppeteer.dao.model.LvmamaScenicSpot;
import com.biji.puppeteer.service.ElasticSearchService;
import com.biji.puppeteer.service.dto.LvmamaScenicSpotDTO;
import com.biji.puppeteer.util.DateUtil;
import com.biji.puppeteer.util.PageResult;
import com.biji.puppeteer.web.vo.LvmamaScenicSpotVO;
import com.google.common.collect.Lists;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * create by biji.zhao on 2020/11/12
 */
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private static final String index = "test";

    @Autowired
    ElasticSearchMapper elasticSearchMapper;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    LvmamaScenicSpotMapper lvmamaScenicSpotMapper;

    @Autowired
    ScenicSpotEsMapper scenicSpotEsMapper;

    private static final String ik_index = "lvmama_scenic_spot";

    @Override
    public void importLvmama() throws Exception {
        List<LvmamaScenicSpot> scenicSpotList = lvmamaScenicSpotMapper.findAll();
        // 如此创建的对象才能被分词
        XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                        .field("properties")
                            .startObject()
                                .field("common")
                                    .startObject()
                                        .field("index", "true").field("type", "keyword")
                                    .endObject()
                                .field("title")
                                    .startObject()
                                        .field("index", "true").field("type", "text").field("analyzer", "ik_max_word")
                                    .endObject()
                            .endObject()
                    .endObject();
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(ik_index);
            createIndexRequest.mapping(builder);
            restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

        for (LvmamaScenicSpot s : scenicSpotList) {
            IndexRequest indexRequest = new IndexRequest(ik_index);
            String userJson = JSONObject.toJSONString(toVO(s));
            indexRequest.source(userJson, XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }

    private static LvmamaScenicSpotVO toVO(LvmamaScenicSpot model) {
        if (model == null) {
            return null;
        }
        LvmamaScenicSpotVO scenicSpotVO = new LvmamaScenicSpotVO();
        scenicSpotVO.setId(model.getId());
        scenicSpotVO.setTitle(model.getTitle());
        scenicSpotVO.setPrice(model.getPrice());
        scenicSpotVO.setPhotoUrl(model.getPhotoUrl());
        scenicSpotVO.setCommon(model.getCommon());
        scenicSpotVO.setCreateTime(DateUtil.formatDate(model.getCreateTime()));
        scenicSpotVO.setUpdateTime(DateUtil.formatDate(model.getUpdateTime()));
        return scenicSpotVO;
    }

    @Override
    public void deleteAll() {
        scenicSpotEsMapper.deleteAll();
    }

    @Override
    public PageResult<LvmamaScenicSpotDTO> queryScenicSpot(String title, Integer pageStart, Integer pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ik_index);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", title);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").preTags("<font color='red'>").postTags("</font>");
        searchSourceBuilder.query(queryBuilder);

        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] hits = response.getHits().getHits();
        List<EsScenicSpot> scenicSpots = Lists.newArrayList();
        for (SearchHit hit : hits) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField productName = highlightFields.get("title");
            StringBuilder newName = new StringBuilder();
            if (productName != null){
                //获取该高亮字段的高亮信息
                Text[] fragments = productName.getFragments();
                //将前缀、关键词、后缀进行拼接
                for (Text fragment : fragments) {
                    newName.append(fragment);
                }
            }
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            sourceAsMap.put("title", newName.toString());
            String json = JSON.toJSONString(sourceAsMap);
            EsScenicSpot esScenicSpot = JSON.parseObject(json, EsScenicSpot.class);
            scenicSpots.add(esScenicSpot);
        }
        PageResult<LvmamaScenicSpotDTO> result = new PageResult<>();
        result.setTotalRecordCount(Math.toIntExact(response.getHits().getTotalHits().value));
        result.setRecords(Lists.newArrayList(scenicSpots.stream()
                .map(ElasticSearchServiceImpl::toScenicSpot).collect(Collectors.toList())));
        return result;
    }

    private static LvmamaScenicSpotDTO toScenicSpot(EsScenicSpot esScenicSpot) {
        if (esScenicSpot == null) {
            return null;
        }
        LvmamaScenicSpotDTO lvmamaScenicSpotDTO = new LvmamaScenicSpotDTO();
        lvmamaScenicSpotDTO.setId(Math.toIntExact(esScenicSpot.getId()));
        lvmamaScenicSpotDTO.setTitle(esScenicSpot.getTitle());
        lvmamaScenicSpotDTO.setPrice(esScenicSpot.getPrice());
        lvmamaScenicSpotDTO.setPhotoUrl(esScenicSpot.getPhotoUrl());
        lvmamaScenicSpotDTO.setCommon(esScenicSpot.getCommon());
        lvmamaScenicSpotDTO.setCreateTime(DateUtil.parseDate(esScenicSpot.getCreateTime()));
        lvmamaScenicSpotDTO.setUpdateTime(DateUtil.parseDate(esScenicSpot.getUpdateTime()));
        return lvmamaScenicSpotDTO;
    }

    @Override
    public Boolean deleteIndex() throws Exception {
        return restHighLevelClient.indices()
                .delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT).isAcknowledged();
    }

    @Override
    public Boolean searchIndex() throws Exception {
        return restHighLevelClient.indices()
                .exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
    }

    @Override
    public String createIndex() throws Exception {
        if (searchIndex()) {
            return "索引已经存在";
        }

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);

        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", 5)
                .put("index.number_of_replicas", 1)
        );

        createIndexRequest.mapping(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject("properties")
                        .startObject("id").field("type", "keyword").field("index", false).endObject()
                        .startObject("pid").field("type", "long").field("index", false).endObject()
                        .startObject("weightid").field("type", "long").field("index", false).endObject()
                        .startObject("count").field("type", "long").field("index", false).endObject()
                        .startObject("addTime").field("type", "text").field("index", false).endObject()
                        .startObject("type").field("type", "integer").field("index", false).endObject()
                        .startObject("video").field("type", "text").field("index", false).endObject()
                        .startObject("context").field("type", "text")
                                .field("analyzer", "ik_max_word")
                                .field("search_analyzer", "ik_smart").endObject()
                        .startObject("cover").field("type", "text").field("index", false).endObject()
                        .startObject("label").field("type", "text").field("index", false).endObject()
                        .startObject("skip").field("type", "text").field("index", false).endObject()
                        .startObject("extend").field("type", "text").field("index", false).endObject()
                        .startObject("fileUrl").field("type", "text").field("index", false).endObject()
                        .startObject("fileUrlEn").field("type", "text").field("index", false).endObject()
                        .startObject("imgs").field("type", "text").field("index", false).endObject()
                        .startObject("title").field("type", "text")
                                .field("analyzer", "ik_max_word")
                                .field("search_analyzer", "ik_smart").endObject()
                        .startObject("downloadCount").field("type", "long").field("index", false).endObject()
                        .startObject("name").field("type", "text").field("index", false).endObject()
                        .startObject("enName").field("type", "text").field("index", false).endObject()
                        .startObject("subhead").field("type", "text").field("index", false).endObject()
                        .startObject("icon").field("type", "text").field("index", false).endObject()
                        .startObject("uri").field("type", "text").field("index", false).endObject()
                        .endObject().endObject());

        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

        return createIndexResponse.index();
    }
}
