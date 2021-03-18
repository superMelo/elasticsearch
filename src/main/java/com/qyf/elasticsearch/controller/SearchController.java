package com.qyf.elasticsearch.controller;

import com.alibaba.fastjson.JSON;
import com.qyf.elasticsearch.entity.Article;
import com.qyf.elasticsearch.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
public class SearchController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("save")
    public void save(){
        Article article = new Article();
        article.setId(UUID.randomUUID().toString());
        article.setTitle("天天念叨JDM的好，这回丰田的发动机也JDM了，上车不？");
        articleService.saveDoc("article", JSON.toJSONString(article));
    }

    @RequestMapping("delete")
    public void delete(){
        articleService.deleteIndex("article");
    }

    @RequestMapping("find")
    public List<Article> find(){
        List<Article> doc = articleService.getDoc("article", "发动机");
        return doc;
    }
}
