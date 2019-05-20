package com.furca.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "myAuthor", types = { Author.class }) 
public interface MyAuthor {
  Long getId();
  String getName();
  String getAge();
  Set<Book> getBooks();
}

