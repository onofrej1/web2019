package com.furca.repository;

import com.furca.model.Result;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ResultRepository extends PagingAndSortingRepository<Result, Long> {
	
}