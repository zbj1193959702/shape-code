package com.biji.puppeteer.shapecode.service.impl;

import com.alibaba.fastjson.JSON;
import com.biji.puppeteer.shapecode.dao.mapper.ElasticSearchMapper;
import com.biji.puppeteer.shapecode.dao.model.ElasticSearchParams;
import com.biji.puppeteer.shapecode.service.ElasticSearchService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * create by biji.zhao on 2020/11/12
 */
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Value("${elasticsearch.index}")
    private String index;

    @Autowired
    ElasticSearchMapper elasticSearchMapper;

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean addData() throws Exception {
        List<ElasticSearchParams> params = elasticSearchMapper.getAllDataList();
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        boolean exists = restHighLevelClient.indices()
                .exists(getIndexRequest, RequestOptions.DEFAULT);
        if (!exists) {
            createIndex();
        }
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueMinutes(5));
        params.forEach(p ->
                bulkRequest.add(new IndexRequest(index)
                        .id(p.getId().toString())
                        .source(JSON.toJSONString(p), XContentType.JSON)));
        BulkResponse responses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return responses.hasFailures();
    }



    @Override
    public List<Map<String, Object>> searchData(String keyword, int pageNo, int pageSize) throws Exception {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(getSearchSourceBuilder(keyword, pageNo, pageSize));
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String, Object>> resultList = Lists.newArrayList();

        Lists.newArrayList(response.getHits().getHits()).forEach(filed -> {
            Map<String, HighlightField> highlightFields = filed.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            HighlightField content = highlightFields.get("content");

            Map<String, Object> one = Maps.newHashMap();
            if (title != null) {
                StringBuilder titleBuilder = new StringBuilder();
                Lists.newArrayList(title.fragments()).forEach(e -> titleBuilder.append(e.toString()));
                one.put("title", titleBuilder.toString());
            }
            if (content != null) {
                StringBuilder contentBuilder = new StringBuilder();
                Lists.newArrayList(content.fragments()).forEach(e -> contentBuilder.append(e.toString()));
                one.put("content", contentBuilder.toString());
            }
            resultList.add(one);

        });
        return resultList;
    }

    @Override
    public Boolean deleteIndex() throws Exception {
        return restHighLevelClient.indices()
                .delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT).isAcknowledged();
    }

    @Override
    public Boolean searchIndex() throws Exception {
        return restHighLevelClient.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
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

    private static SearchSourceBuilder getSearchSourceBuilder(String keyword, int pageNo, int pageSize) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(pageNo -1);
        searchSourceBuilder.size(pageSize);
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery(keyword, "title", "content").field("title", 10);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchSourceBuilder.highlighter(getHighlightBuilder());
        return searchSourceBuilder;
    }

    private static HighlightBuilder getHighlightBuilder() {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.fields().add(new HighlightBuilder.Field("title"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("content"));
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color=red\">");
        highlightBuilder.postTags("</span>");
        return highlightBuilder;
    }
}
