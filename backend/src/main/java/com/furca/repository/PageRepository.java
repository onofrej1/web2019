package com.furca.repository;

import com.furca.model.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PageRepository extends PagingAndSortingRepository<Page, Long> {
	
	
}