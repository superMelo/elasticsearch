package com.qyf.elasticsearch.service.impl;


import com.qyf.elasticsearch.entity.Article;
import com.qyf.elasticsearch.repository.EsRepository;
import com.qyf.elasticsearch.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {


    @Autowired
    private EsRepository repository;

    @Override
    public void createIndex(String indexName) {
        repository.createIndex(indexName);
    }

    @Override
    public void deleteIndex(String indexName) {
        repository.deleteIndex(indexName);
    }

    @Override
    public boolean checkIndex(String indexName) {
        return repository.checkIndex(indexName);
    }

    @Override
    public void saveDoc(String indexName, String jsonStr) {
        repository.saveDoc(indexName, jsonStr);
    }

    @Override
    public void deleteDoc(String indexName, String id) {
        repository.deleteDoc(indexName, id);
    }

    @Override
    public List<Article> getDoc(String indexName, String title) {
        return repository.getDoc(indexName, title);
    }
}
