package com.qyf.elasticsearch.service;



import com.qyf.elasticsearch.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    void createIndex(String indexName);

    void deleteIndex(String indexName);

    boolean checkIndex(String indexName);

    void saveDoc(String indexName, String jsonStr);

    void deleteDoc(String indexName, String id);

    List<Article> getDoc(String indexName, String title);
}
