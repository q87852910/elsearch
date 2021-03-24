package com.example.elsearch.service;

import com.alibaba.fastjson.JSON;
import com.example.elsearch.User.Content;
import com.example.elsearch.User.User;
import com.example.elsearch.Utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ContentSevice
 * @Description: TODO
 * @author: 大佬
 * @Date: 2021/3/24 23:01
 * @Version: 1.0
 **/
@Service
public class ContentService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    //解析到索引中
    public boolean parseContent(String keywords) throws IOException {
        List<Content> list = HtmlParseUtil.parseJD(keywords);
        BulkRequest request = new BulkRequest();
        request.timeout("2m");
        for(int i=0;i<list.size();i++){
            request.add(new IndexRequest("jingdong-list").id(""+(i+1)).
                    source(JSON.toJSONString(list.get(i)), XContentType.JSON));

        }
        BulkResponse bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        return bulk.hasFailures();
    }
    //获取数据实现搜索功能
    public List<Map<String,Object>> searchPage(String keyword,int pageNo,int pageSize) throws IOException {
        if(pageNo<=1){
            pageNo=1;
        }
        //条件搜索
        SearchRequest request = new SearchRequest("jingdong-list");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //分页
        builder.from(pageNo);
        builder.size(pageSize);
        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //查询条件
        TermQueryBuilder query = QueryBuilders.termQuery("title", keyword);
        builder.query(query);
        //执行搜素
        request.source(builder);
        //执行结果
        SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        //拿到结果
        List<Map<String,Object>> list =new ArrayList<>();
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            list.add(sourceAsMap);
        }
        return list;
    }
}
