package com.furca.repository;

import com.furca.model.Result;
import com.furca.model.ResultProjection;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(excerptProjection = ResultProjection.class)
public interface ResultRepository extends PagingAndSortingRepository<Result, Long> {
	
	@RestResource(path = "run", rel = "run")
	public Page<Result> findByRunId(@Param("id") Long id, Pageable pageable);
}