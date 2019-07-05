package com.furca.repository;

import com.furca.model.Author;
import com.furca.model.AuthorProjection;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = AuthorProjection.class)
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
	
}