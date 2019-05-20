package com.furca.repository;

import com.furca.model.Author;
import com.furca.model.MyAuthor;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin("http://192.168.10.10:8081")
@RepositoryRestResource(excerptProjection = MyAuthor.class)
public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer> {
	
}