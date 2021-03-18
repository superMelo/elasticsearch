package com.qyf.elasticsearch.repository;

import com.qyf.elasticsearch.entity.Article;

import java.util.List;
import java.util.Map;

public interface EsRepository {
    void createIndex(String indexName);

    void deleteIndex(String indexName);

    boolean checkIndex(String indexName);

    void saveDoc(String indexName, String jsonStr);

    void deleteDoc(String indexName, String jsonStr);

    List<Article> getDoc(String indexName, String title);
}
