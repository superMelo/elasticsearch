package com.qyf.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.qyf.elasticsearch.entity.Article;
import com.qyf.elasticsearch.repository.EsRepository;
import com.qyf.elasticsearch.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@SpringBootTest
class ElasticsearchApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private ArticleService articleService;

	@Autowired
	private EsRepository articleRepository;


	@Test
	public void saveIndex(){
		articleRepository.createIndex("article");
	}

	@Test
	public void deleteIndex(){
		articleRepository.deleteIndex("article");
	}

	@Test
	public void saveDoc(){
		Article article = new Article();
		article.setTitle("新疆的西红柿");
		article.setId(UUID.randomUUID().toString());
		articleRepository.saveDoc("article", JSON.toJSONString(article));
	}


	@Test
	public void getDoc(){
		List<Article> doc = articleRepository.getDoc("article", "番茄");
		System.out.println(doc);
	}
}
