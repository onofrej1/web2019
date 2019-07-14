package com.furca.model;

import java.util.Date;
import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "ArticleProjection", types = { Article.class }) 
public interface ArticleProjection {
  Long getId();
  String getTitle();
  Category getCategory();
  String getContent();
  String getPublishedBy();
  String getSource();
  Date getPublishedOn();
  Set<Tag> getTags();
}