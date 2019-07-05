package com.furca.model;

import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "AuthorProjection", types = { Author.class }) 
public interface AuthorProjection {
  Long getId();
  String getName();
  String getAge();
  Set<Book> getBooks();
}

