package com.furca.repository;

import com.furca.model.Author;
import com.furca.model.MyAuthor;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(excerptProjection = MyAuthor.class)
public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer> {
	
}