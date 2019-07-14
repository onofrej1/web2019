package com.furca.repository;

import com.furca.model.Article;
import com.furca.model.ArticleProjection;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = ArticleProjection.class)
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {
	
}