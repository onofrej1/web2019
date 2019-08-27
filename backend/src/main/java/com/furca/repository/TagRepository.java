package com.furca.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.furca.model.Result;
import com.furca.model.Tag;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
	
	@RestResource
	public Page<Tag> findByTag(@Param("search") String search, Pageable pageable);
	
}
