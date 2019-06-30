package com.furca.repository;

import com.furca.model.Result;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

public interface ResultRepository extends PagingAndSortingRepository<Result, Integer> {
	
}