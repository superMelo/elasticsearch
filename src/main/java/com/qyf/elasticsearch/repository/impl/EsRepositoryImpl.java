package com.qyf.elasticsearch.repository.impl;

import com.qyf.elasticsearch.entity.Article;
import com.qyf.elasticsearch.repository.EsRepository;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class EsRepositoryImpl implements EsRepository {

    @Autowired
    private RestHighLevelClient restHighLevelClient;



    private String jsonFileReader(File file){
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }
    private String getJsonSetting() {
        File file;
        try {
            file = ResourceUtils.getFile("classpath:elasticsearch/search.json");
            String s = this.jsonFileReader(file);
            return s;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    //创建索引
    @Override
    public void createIndex(String indexName) {
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        try {
            //设置索引配置
            indexRequest.source(getJsonSetting(), XContentType.JSON);
            restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除索引
    @Override
    public void deleteIndex(String indexName) {
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
        try {
            restHighLevelClient.indices().delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //检查索引
    @Override
    public boolean checkIndex(String indexName) {
        GetIndexRequest request = new GetIndexRequest(indexName);
        try {
            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //保存文档
    @Override
    public void saveDoc(String indexName, String jsonStr) {
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(UUID.randomUUID().toString());
        indexRequest.source(jsonStr, XContentType.JSON);
        try {
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除文档
    @Override
    public void deleteDoc(String indexName, String jsonStr) {
        DeleteRequest deleteRequest = new DeleteRequest(indexName);
        try {
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取文档
    @Override
    public List<Article> getDoc(String indexName, String title) {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MatchQueryBuilder query = QueryBuilders.matchQuery("title", title);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<e>");
        highlightBuilder.postTags("</e>");
        builder.highlighter(highlightBuilder);
        builder.query(query).from(0).size(5);
        searchRequest.source(builder);
        List<Article> mapList = new LinkedList<>();
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            for (SearchHit hit : hits) {
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                Map<String, Object> map = hit.getSourceAsMap();
                HighlightField field = highlightFields.get("title");
                Article article = new Article();
                article.setId((String) map.get("id"));
                article.setTitle(field.getFragments()[0].string());
                mapList.add(article);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }
}
